import java.util.Map;

class Config {
  String configFile;
  HashMap<String,String> hm;

  Config(String cfgFile) {
    configFile = cfgFile;
    hm = new HashMap<String,String>();
  }

  void load(){
    String line;
    String[] keyValue;

    try {
      String lines[] = loadStrings(configFile);
      for (int i=0; i < lines.length; i++) {
        line = trim(lines[i]);

        if ( ( line.indexOf("#") != 0 ) &&  ( line.indexOf(":") > 0 ) )  {
          println("Have line '" + line + "'");
          keyValue = split(lines[i], ':'); 
          hm.put(trim(keyValue[0]), trim(keyValue[1]));
        }
      }
    } catch(Exception e) {
      println("Error loading data from '" + configFile + "'");
      e.printStackTrace();
    }
  }


  int intValue(String k) {
    return parseInt( hm.get(k) );
  }

  float floatValue(String k) {
    return parseFloat( hm.get(k) );
  }

  String value(String k) {
    return hm.get(k);
  }
}

