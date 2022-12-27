public class ConsoleWriter {
    public void writelnAndExit(String str) {
        this.writelnError(str);
        System.exit(1);
    }

    public void writelnError(String output) {
        this.writeln("ERROR: " + output);
    }

    public void writeln(String str) {
        System.out.println(str);
    }
}
