class LaboonCrypt {

    private static int verbose = 0;
    private static LaboonHash lh = new LaboonHash();

    public static void main(String[] args) {
        if (args.length < 1 || args.length > 2) {
            System.out.println(
                    "Usage:\njava LaboonCrypt *string* *verbosity_flag*\nVerbosity flag can be omitted for hash output only\nOther options: -verbose -veryverbose -ultraverbose");
            return;
        }
        if (args.length == 2) {
            switch (args[1]) {
            case "-verbose":
                verbose = 1;
                break;
            case "-veryverbose":
                verbose = 2;
                break;
            case "-ultraverbose":
                verbose = 3;
                lh.verbose = true;
                break;
            default:
                System.out.println(
                        "Usage:\njava LaboonCrypt *string* *verbosity_flag*\nVerbosity flag can be omitted for hash output only\nOther options: -verbose -veryverbose -ultraverbose");
                return;
            }
        }
        String finalhash = hash(args[0]);
        System.out.println("\n\nLaboonCrypt hash: " + finalhash);
    }

    public static String hash(String toHash) {
        // set up IV
        String[][] out = getInitialArr(toHash);
        out = reRead(toHash, out);
        if (verbose >= 1) {
            System.out.println("Final Array:");
            printArr(out);
        }
        return getFinalHash(out);
    }

    private static String getFinalHash(String[][] out) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[0].length; j++) {
                sb.append(out[i][j]);
            }
        }
        return lh.hash(sb.toString());
    }

    private static String[][] getInitialArr(String toHash) {
        String[][] out = new String[12][12];
        String oldVal = lh.hash(toHash);
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[0].length; j++) {
                out[i][j] = oldVal;
                oldVal = lh.hash(out[i][j]);
            }
        }
        if (verbose >= 1) {
            System.out.println("Inital Array:");
            printArr(out);
        }
        return out;
    }

    private static String[][] reRead(String toHash, String[][] out) {
        int down = 0, right = 0;
        for (int i = 0; i < toHash.length(); i++) {
            char c = toHash.charAt(i);
            down = ((c * 11) + down) % 12;
            right = (((c + 3) * 7) + right) % 12;
            String oldVal = out[down][right];
            String newVal = lh.hash(oldVal);
            if (verbose >= 2) {
                System.out.println("Moving " + (c * 11) + " down and " + (c + 3) * 7 + " right - modifying [" + down
                        + ", " + right + "] from " + oldVal + " to " + newVal);
            }
            out[down][right] = newVal;
        }
        return out;
    }

    private static void printArr(String[][] ar) {
        for (int i = 0; i < ar.length; i++) {
            System.out.println("");
            for (int j = 0; j < ar[0].length; j++) {
                System.out.print(ar[i][j] + " ");
            }
        }
        System.out.println("\n");
    }
}