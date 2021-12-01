require_relative './client'
require_relative './settings'
require_relative './filesystem'

class Scraper
  def initialize(client)
    @client = client
  end

  def scrape(position, proxy, url)
    attempt = 0

    loop do
      response = @client.fetch_page(proxy, url)
      if response != nil && response.code != "200"
        write_error_to_file("#{url} - Response code #{response.code}")
        write_error_to_stdout("#{url} - Response code #{response.code}")
      end

      if response != nil && response.code == "200"
        write_success_to_file(position, response.body)
        break
      end

      attempt += 1
      if attempt >= get_setting(:RetriesNum)
        break
      end
    end
  end
end
