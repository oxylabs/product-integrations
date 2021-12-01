def write_error_to_stdout(message)
  p "ERROR: #{message}"
end

def write_error_to_file(message)
  error_filename = "failed_requests.txt"

  open(error_filename, 'a') { |f|
    f.puts message + "\n"
  }
end

def write_success_to_file(position, content)
  open("result_#{position}.html", 'w') { |f|
    f.puts content
  }
end
