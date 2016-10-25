require 'csv'
require 'net/http'
require 'rest_client'
require 'json'

# check for correct number of arguments
unless ARGV.length == 2
  puts "Usage: ruby run_samples.rb inputFile.csv server \n"
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
                  {
                    type: area_type,
                    code: area_code
                  }.to_json, content_type: :json, accept: :json
                ) do |put_response, _request, _result, &_block|
    if put_response.code == 200
      puts "Successfully run sample for sampleId  #{sample_id}"
    else
      puts "Failed to run sample for sampleId #{sample_id}"
    end
  end
end
