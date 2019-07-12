package warp;
/**
 * https://www.typescriptlang.org/docs/handbook/tsconfig-json.html
 *
 *
 */

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

final public class TSConfig {
    private Logger log = Logger.getLogger(TSConfig.class);
    private JSONObject json;
    private JSONObject compilerOptions;
    private Path rootDir;
    private Path outDir;

    public TSConfig(String filename) {
        readConfig(Path.of(filename));
    }
    public Path getRootDirectory() {
        return rootDir;
    }
    public Path getOutputDirectory() {
        return outDir;
    }
    public List<File> getFiles() {
        if(json==null) return List.of();
        return toList(json.optJSONArray("files"))
            .stream()
            .map((e)->rootDir.resolve((String)e).toFile())
            .collect(Collectors.toList());
    }
    public List<String> getIncludes() {
        if(json==null) return List.of();
        return toList(json.optJSONArray("includes"));
    }
    public List<String> getExcludes() {
        if(json==null) return List.of();
        return toList(json.optJSONArray("excludes"));
    }
    public List<String> getTypeRoots() {
        if(compilerOptions==null) return List.of();
        return toList(compilerOptions.optJSONArray("typeRoots"));
    }
    public List<String> getTypePackages() {
        if(compilerOptions==null) return List.of();
        return toList(compilerOptions.optJSONArray("types"));
    }

    private void readConfig(Path path) {
        try{
            var content = removeComments(Files.readAllBytes(path));

            this.json = new JSONObject(content);
            this.compilerOptions = json.optJSONObject("compilerOptions");

            this.rootDir = path.normalize().getParent();

            Supplier<Path> outDirSupplier = ()-> {
                if(compilerOptions==null) return rootDir.resolve("out");
                return rootDir.resolve(compilerOptions.optString("outDir", "out"));
            };
            this.outDir = outDirSupplier.get();

            log.debug("json: "+json);
            log.debug("rootDir: "+rootDir);
            log.debug("outDir: "+outDir);

        }catch(Exception e) {
            log.error(String.format("Unable to parseMultiple json file: '{%s}'", path), e);
        }
    }
    private String removeComments(byte[] bytes) {
        int dest = 0;
        boolean inQuotes = false;

        for(var i=0; i<bytes.length; i++) {
            var b = bytes[i];
            switch(b) {
                case '\"':
                    if(i>0 && bytes[i-1]=='\\') {
                    } else {
                        inQuotes = !inQuotes;
                    }
                    bytes[dest++] = b;
                    break;
                case '/':
                    if(!inQuotes && i<bytes.length-1) {
                        if(bytes[i+1]=='/') {
                            // Line comment
                            while(i<bytes.length && bytes[i]!=10 && bytes[i]!=13) {
                                i++;
                            }
                            continue;
                        } else if(bytes[i+1]=='*') {
                            // Block comment
                            i+=2;
                            while(i<bytes.length && (bytes[i]!='/' || bytes[i-1]!='*')) {
                                i++;
                            }
                            continue;
                        }
                    }
                    bytes[dest++] = b;
                    break;
                default:
                    bytes[dest++] = b;
                    break;
            }
        }
        return new String(bytes, 0, dest);
    }
    private <T> List<T> toList(JSONArray ja) {
        if(ja==null || ja.length()==0) return List.of();

        var list = new ArrayList<T>(ja.length());
        for(var i = 0; i < ja.length(); i++) {
            list.add((T)ja.get(i));
        }
        return list;
    }
}
