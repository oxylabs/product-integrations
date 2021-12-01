require_relative './settings'

def format_proxy(proxy)
  "http://#{get_setting(:Username)}:#{get_setting(:Password)}@#{proxy}"
end