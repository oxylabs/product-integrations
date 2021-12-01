require_relative './settings'

def create_proxy_by_url(url)
  url_parts = url.split ';'
  if url_parts.length == 2
    proxy_address = "http://customer-#{get_setting(:Username)}-cc-#{url_parts[1]}:#{get_setting(:Password)}@#{get_setting(:ProxyAddress)}"

    return [url_parts[0], proxy_address]
  end

  proxy_address = "http://customer-#{get_setting(:Username)}:#{get_setting(:Password)}@#{get_setting(:ProxyAddress)}"

  [url, proxy_address]
end

# func createProxyByUrl(url string) (string, string) {
#   urlParts := strings.Split(url, ";")
# if len(urlParts) == 2 {
#   return urlParts[0], fmt.Sprintf(
#     "http://customer-%s-cc-%s:%s@%s",
#     Username,
#     urlParts[1],
#     Password,
#     ProxyAddress,
#     )
# }
#
# return url, fmt.Sprintf(
#   "http://customer-%s:%s@%s",
#   Username,
#   Password,
#   ProxyAddress,
#   )
# }