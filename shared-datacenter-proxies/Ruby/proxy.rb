require_relative './settings'

def create_proxy_by_url(url)
  proxy_address = "http://customer-#{get_setting(:Username)}:#{get_setting(:Password)}@#{get_setting(:ProxyAddress)}"

  [url, proxy_address]
end