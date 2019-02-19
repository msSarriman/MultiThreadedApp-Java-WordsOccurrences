package counting.files.multithread;

/**
 * CheckOS
 * An instance of this class will provide information about the System the program is running onto.
 */
public class CheckOS {
    private String OPERATING_SYSTEM;


    CheckOS(){
        String tempOS = System.getProperty("os.name").toLowerCase();
        if (tempOS.contains("win")) OPERATING_SYSTEM = "win";
        else if (tempOS.contains("nix") || tempOS.contains("nux")) OPERATING_SYSTEM = "linux";
        else if (tempOS.contains("mac")) OPERATING_SYSTEM = "mac";
        else OPERATING_SYSTEM = "other";
    }


    /**
     * returns `win` for windows
     *  ->>-   `linux` for linux or unix
     *  ->>-   `mac` for MacOS
     *  ->>-   `other` for Solaris,BSD e.t.c.
     */
    protected String getOS(){
        return OPERATING_SYSTEM;
    }
}
