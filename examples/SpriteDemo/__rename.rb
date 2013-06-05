base = 'phxCC_ts001'

Dir.chdir 'data' do 
  Dir.glob("#{base}*").each do |f|
    p f
    _ = f.sub base , 'sprite'
    File.rename f , _
  end

end
