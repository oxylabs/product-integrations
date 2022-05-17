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

p 'Gathering results...'
operations = []
urls.each_with_index do |url, position|
  parsed_url, formatted_proxy = create_proxy_by_url(url)

  operation = Concurrent::Future.execute do
    scraper.scrape(position, formatted_proxy, parsed_url)

    requests_rate = get_setting(:RequestsRate)
    sleep(1 / requests_rate)
  end

  operations.push(operation)
end

operations.each { |operation| operation.value }

ending = Time.now
elapsed = ending - starting
printf "Script finished after %.2fs\n", elapsed
