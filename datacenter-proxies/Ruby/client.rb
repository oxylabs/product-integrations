require 'uri'
require 'net/http'
require_relative './settings'
require_relative './header'

class Client
  def fetch_proxies
    url = 'https://proxy.oxylabs.io/all'
    parsed_url = URI.parse(url)

    response = fetch(parsed_url, get_setting(:Username), get_setting(:Password))

    response.body.split("\n")
  end

  def get_timeout
    get_setting(:Timeout) * 1000
  end

  def fetch(url, username = '', password = '', limit = 3)
    raise ArgumentError, 'Too many redirects!' if limit == 0

    header_generator = HeaderGenerator.new
    random_headers = header_generator.generate_random_browser_headers

    req = Net::HTTP::Get.new(url.path, random_headers)
    req.basic_auth(username, password)
    response = Net::HTTP.start(url.host, url.port, {
        use_ssl: true,
        read_timeout: get_timeout,
        open_timeout: get_timeout,
        write_timeout: get_timeout
    }) do
    |http|
      http.request(req)
    end

    case response
    when Net::HTTPSuccess then response
    when Net::HTTPRedirection then fetch(response['location'], username, password, limit - 1)
    else
      response.error!
    end
  end

  def fetch_page(raw_proxy, raw_url)
    parsed_proxy = URI.parse(raw_proxy)
    proxy = Net::HTTP::Proxy(parsed_proxy.host,
                             parsed_proxy.port,
                             parsed_proxy.user,
                             parsed_proxy.password)
    url = URI.parse(raw_url)

    fetch_with_proxy(proxy, url)
  end

  def fetch_with_proxy(proxy, url, limit = 3)
    raise ArgumentError, 'Too many redirects!' if limit == 0

    if url.path === ''
      url.path = '/'
    end

    header_generator = HeaderGenerator.new
    random_headers = header_generator.generate_random_browser_headers

    req = Net::HTTP::Get.new(url.path, random_headers)
    response = proxy.start(url.host, url.port, {
        use_ssl: true,
        read_timeout: get_timeout,
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
