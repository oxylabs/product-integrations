require_relative './settings'

def create_proxy_by_url(proxy_map, url)
  url_parts = url.split ';'
  default_proxy_index = get_setting :DefaultProxyIndexName

  country = nil
  if url_parts.length == 2
    url = url_parts[0]
    country = url_parts[1]
  end

  proxy_address = proxy_map[default_proxy_index]
  if proxy_map.key? country
    proxy_address = proxy_map[country]
  end

  proxy_address = "http://customer-#{get_setting(:Username)}:#{get_setting(:Password)}@#{proxy_address}"

  [url, proxy_address]
end