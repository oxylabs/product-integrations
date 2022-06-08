import asyncio
import re
import sys
import time
from dataclasses import dataclass
from typing import List, Tuple, Union, Dict

import aiohttp

import headers

USERNAME = ""
PASSWORD = ""
REQUESTS_RATE = 10
TIMEOUT = 5
RETRIES_NUM = 3
URL_LIST_NAME = "url_list_shared_dc.txt"
PROXY_LIST_NAME = "proxy_list_shared_dc.txt"

DEFAULT_PROXY_INDEX_NAME = 'DEFAULT'

def read_url_list() -> List[str]:
    print("Reading URL list...")
    try:
        with open(URL_LIST_NAME) as urls_list:
            return [url.strip() for url in urls_list.readlines()]
    except FileNotFoundError:
        print(f"File `{URL_LIST_NAME}` not found.")
        sys.exit(1)


def read_proxy_map() -> Dict[str, str]:
    print("Reading Proxy list...")
    pattern = r'^dc\.(\w{2})-?pr\.oxylabs\.io:\d+$'

    try:
        proxy_map = {}
        with open(PROXY_LIST_NAME) as proxy_list:
            for proxy_url in proxy_list.readlines():
                stripped_proxy_url = proxy_url.strip()
                matches = re.findall(pattern, stripped_proxy_url)
                if not matches:
                    proxy_map[DEFAULT_PROXY_INDEX_NAME] = stripped_proxy_url
                    continue

                [country] = matches
                proxy_map[country.upper()] = stripped_proxy_url
        return proxy_map

    except FileNotFoundError:
        print(f"File `{URL_LIST_NAME}` not found.")
        sys.exit(1)


@dataclass
class Response:
    url: str
    status: int = None
    exception: str = None
    text: str = None


async def scrape_page(
        timeout: aiohttp.ClientTimeout,
        url: str,
        proxy_map: Dict[str, str]
) -> Tuple[Union[int, str], str]:
    country = None
    split_result = url.split(";")
    if len(split_result) > 1:
        url, country = split_result

    proxy_address = proxy_map[DEFAULT_PROXY_INDEX_NAME]
    if country is not None and country in proxy_map:
        proxy_address = proxy_map[country]

    proxy = f"http://customer-{USERNAME}:{PASSWORD}@{proxy_address}"
    fingerprint = await headers.get_fingerprint()
    try:
        async with aiohttp.ClientSession(timeout=timeout) as session:
            async with session.get(url, proxy=proxy, headers=fingerprint) as resp:
                body = await resp.text()
                return Response(url=url, status=resp.status, text=body)
    except Exception as err:
        return Response(url=url, exception=str(err))


async def append_failed_requests(message: str) -> None:
    with open("failed_requests.txt", "a") as failed_requests_file:
        failed_requests_file.write(message)


async def handle_request_response(timeout: aiohttp.ClientTimeout,
                                  id: int,
                                  url: str,
                                  proxy_map: Dict[str, str],
                                  ) -> None:
    response = await scrape_page(timeout=timeout, url=url, proxy_map=proxy_map)
    retries = 0
    while response.status != 200 and retries < RETRIES_NUM:
        response = await scrape_page(timeout=timeout, url=url, proxy_map=proxy_map)
        retries += 1
    if response.status:
        if response.status == 200:
            with open(f"result_{id}.html", "w") as result_file:
                result_file.write(response.text)
        else:
            await append_failed_requests(message=f"{url} - Response code {response.status}\n")
    if response.exception:
        await append_failed_requests(message=f"{url} - {response.exception}\n")


async def main() -> None:
    start_time = time.time()
    urls = read_url_list()
    proxy_map = read_proxy_map()
    timeout = aiohttp.ClientTimeout(total=None, sock_connect=TIMEOUT, sock_read=TIMEOUT)
    print("Gathering results...")
    tasks = []
    for id, url in enumerate(urls, 1):
        tasks.append(
            asyncio.create_task(
                handle_request_response(
                    timeout=timeout,
                    id=id,
                    url=url,
                    proxy_map=proxy_map
                )
            )
        )
        await asyncio.sleep(1 / REQUESTS_RATE)
    await asyncio.gather(*tasks)
    end_time = time.time()
    print(f"Scraping time: {end_time - start_time} seconds.")


if __name__ == "__main__":
    sys.exit(asyncio.run(main()))
