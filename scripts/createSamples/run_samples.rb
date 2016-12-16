#!/usr/bin/env ruby
require 'rest_client'
require 'json'
require 'csv'

unless ARGV.length == 2
  puts 'Usage: run_samples.rb <input.csv> <server>'
  exit
end

csv_text = File.read(ARGV[0])
csv = CSV.parse(csv_text, headers: true)
csv.each do |row|
  sample_id = row[0].delete(' ')
  area_type = row[1].delete(' ')
  area_code = row[2].delete(' ')
  server    = ARGV[1]

  RestClient.put("http://#{server}:8171/samples/#{sample_id}",
                 { type: area_type, code: area_code }.to_json,
                 content_type: :json,
                 accept: :json) do |response, _request, _result, &_block|
    if response.code == 200
      puts "Successfully run sample for sample ID  #{sample_id}"
    else
      puts "Failed to run sample for sample ID #{sample_id}"
    end
  end
end
