require 'rexml/document'
include REXML

desc "update build"
task :build do
  Dir.chdir("resources") do
    warn "In 'resources'"

#    build = File.read "build.xml"
#    
#    xml = Document.new build
#    versionNumber = xml.elements["project/property[@name='versionNumber']"]
#    v = versionNumber.attributes["value"]
#    nummers = v.split "."
#    minorVersion = nummers[2]
####    minorVersion = minorVersion.to_i + 1
#    versionNumber.attributes["value"] = nummers[0..1].join(".") + "." + minorVersion.to_s
#    File.open("build.xml", "w") do |file|
#      file.puts xml.to_s
#    end
# This is failing on Win7.  It doesn't even seem to be calling ant ...

    warn "RUBY_PLATFORM = #{RUBY_PLATFORM}"
    if RUBY_PLATFORM =~ /gw32/
      warn "Using ant -propertyfile build.properties.windows  "
      sh "call ant -propertyfile build.properties.windows "
     else
    puts `ant`
    end

  end
  # `scp -r distribution/web/* root@grimaceworks.com:/srv/processing/animata/`
end


task :default => :build


task :ant do
  # Seems that calling ant this way is not the same as calling ant in an actual  terminal. Why?
  #
sh "call ant"
end
