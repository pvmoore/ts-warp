import warp.TSConfig;
import warp.TSWarp;

public class Main {

    public static void main(String[] args) {

        var config = new TSConfig("tests/tsconfig.json");

        System.out.println("files="+config.getFiles());
        System.out.println("rootDir="+config.getRootDirectory());
        System.out.println("outDir="+config.getOutputDirectory());
        System.out.println("typeRoots="+config.getTypeRoots());
        System.out.println("types="+config.getTypePackages());

        System.out.println("\n");

        var warp = new TSWarp(config);
        warp.run();

    }
}
