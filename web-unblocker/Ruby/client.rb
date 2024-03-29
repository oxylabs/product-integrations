require 'uri'
require 'net/http'
require_relative './settings'
require_relative './header'

class Client
  def get_timeout
    get_setting(:Timeout) * 1000
  end

  def fetch_page(raw_proxy, raw_url, country)
    parsed_proxy = URI.parse(raw_proxy)
    proxy = Net::HTTP::Proxy(parsed_proxy.host,
                             parsed_proxy.port,
                             parsed_proxy.user,
                             parsed_proxy.password)

    url = URI.parse(raw_url)

    fetch_with_proxy(proxy, url, 3, country)
  end

  def fetch_with_proxy(proxy, url, limit, country)
    raise ArgumentError, 'Too many redirects!' if limit == 0

    if url.path === ''
      url.path = '/'
    end

    header_generator = HeaderGenerator.new
    random_headers = header_generator.generate_random_browser_headers(country)

    req = Net::HTTP::Get.new(url.path, random_headers)
    response = proxy.start(
      url.host,
      80,
      "http://" + proxy.proxy_address,
      proxy.proxy_port,
      proxy.proxy_user,
      proxy.proxy_pass,
      {
      use_ssl: false,
      read_timeout: get_timeout,
      connect_timeout: get_timeout,
      open_timeout: get_timeout,
      write_timeout: get_timeout
    }) do
    |http|
      http.request(req)
    end

    case response
    when Net::HTTPSuccess then response
    when Net::HTTPRedirection then fetch(proxy, response['location'], limit - 1)
    else
      return response
    end
  rescue => error
    return error.response
  end
end
