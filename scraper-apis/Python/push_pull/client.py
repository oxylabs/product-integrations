import time

import requests

SCRAPER_APIS_BASE_URL = "https://data.oxylabs.io/v1"


class PushPullScraperAPIsClient:
    def __init__(self, username: str, password: str):
        self.username = username
        self.password = password

    def _make_request(self, method: str, url: str, payload: dict = None) -> dict:
        response = requests.request(
            method=method,
            url=url,
            auth=(self.username, self.password),
            json=payload,
        )

        if response.status_code >= 400:
            raise requests.exceptions.HTTPError(response.status_code, response.text)

        return response.json()

    def create_job(self, payload: dict) -> dict:
        return self._make_request("POST", f"{SCRAPER_APIS_BASE_URL}/queries", payload)

    def create_jobs_batch(self, payload: dict) -> dict:
        return self._make_request(
            "POST",
            f"{SCRAPER_APIS_BASE_URL}/queries/batch",
            payload,
        )

    def check_job_status(self, job_id: int) -> str:
        response = self._make_request(
            "GET",
            f"{SCRAPER_APIS_BASE_URL}/queries/{job_id}",
        )

        return response.get("status")

    def get_job_results(self, job_id: int) -> dict:
        return self._make_request(
            "GET",
            f"{SCRAPER_APIS_BASE_URL}/queries/{job_id}/results",
        )

    def wait_for_and_get_job_results(self, job_id: int) -> dict:
        while (status := self.check_job_status(job_id)) == "pending":
            time.sleep(5)

        if status != "done":
            raise Exception(
                f"Job is not done, could not extract results. ID: {job_id}, status: {status}",
            )

        return self.get_job_results(job_id)

    def get_callbacker_ips(self) -> list:
        return self._make_request(
            "GET",
            f"{SCRAPER_APIS_BASE_URL}/info/callbacker_ips",
        )
