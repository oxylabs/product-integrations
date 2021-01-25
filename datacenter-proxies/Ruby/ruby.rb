#!/usr/bin/ruby

require 'uri'
require 'net/http'

uri = URI.parse('https://ip.oxylabs.io')
proxy = Net::HTTP::Proxy('PROXY', 'PORT', 'username', 'pass')

req = Net::HTTP::Get.new(uri.path)

result = proxy.start(uri.host,uri.port, :use_ssl => uri.scheme == 'https') do |http|
    http.request(req)
end

puts result.body