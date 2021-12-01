require_relative './header'
require_relative './settings'
require_relative './client'
require_relative './proxy'
require_relative './scraper'
require 'concurrent'

starting = Time.now

client = Client.new
scraper = Scraper.new(client)

p 'Reading from the list...'
urls = File::readlines(get_setting(:UrlListName), chomp: true)

p 'Retrieving proxy list...'
round_robin_proxies = client.fetch_proxies.cycle

p 'Gathering results...'
operations = []
urls.each_with_index do |url, position|
  proxy = round_robin_proxies.next
  formatted_proxy = format_proxy(proxy)

  operation = Concurrent::Future.execute do
    scraper.scrape(position, formatted_proxy, url)

    requests_rate = get_setting(:RequestsRate)
    sleep(1 / requests_rate)
  end

  operations.push(operation)
end

operations.each { |operation| operation.value }

ending = Time.now
elapsed = ending - starting
printf "Script finished after %.2fs\n", elapsed
