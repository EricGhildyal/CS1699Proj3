class LaboonHash {

    private static final String IV = "1AB0";
    public static boolean verbose = false;

    public static void main(String[] args) {
        if (args.length < 1 || args.length > 2) {
            System.out.println(
                    "Usage:\njava LaboonHash *string* *verbosity_flag*\nVerbosity flag can be omitted for hash output only\nOther options: -verbose");
            return;
        }
        if (args.length == 2) {
            if (args[1].equals("-verbose")) {
                verbose = true;
            } else {
                System.out.println(
                        "Usage:\njava LaboonHash *string* *verbosity_flag*\nVerbosity flag can be omitted for hash output only\nOther options: -verbose");
                return;
            }
        }
        String finalhash = hash(args[0]);
        System.out.println("\nLaboonHash hash = " + finalhash);
    }

    public static String hash(String toHash) {
        toHash = getPaddedStr(toHash);
        if (verbose) {
            System.out.println("Padded string: " + toHash);
        }
        String iv = IV;
        String out = "";
        String[] blocks = getBlocks(toHash);
        if (verbose) {
            System.out.println("Blocks:");
            for (String b : blocks) {
                System.out.println(b);
            }
        }
        for (String block : blocks) {
            out = c(block, iv);
            if (verbose) {
                System.out.println("Iterating with " + iv + " / " + block + " = " + out);
            }
            iv = out;
        }
        return out;
    }

    private static String getPaddedStr(String toHash) {
        int len = toHash.length();
        int overflow = len % 8;
        if (overflow == 0) { // divides by 8 evenly, return
            return toHash;
        }
        Double leftOver = 8.0 - overflow; // find how many places we need to fill
        Double maxHex = len % Math.pow(16.0, leftOver); // get max hex
        String pad = String.format("%08X", maxHex.intValue()); // get value to pad with
        // do some hacky shit with math but also take a substring from the back of a
        // string
        return toHash + pad.substring(overflow, pad.length());
    }

    private static String c(String rhs, String lhs) {
        int[] result = new int[4];
        String out = "";
        // phase 1
        for (int i = 0; i < 4; i++) {
            result[i] = lhs.charAt(i) - '0' + rhs.charAt(3 - i) - '0';
        }
        // phase 2
        for (int i = 0; i < 4; i++) {
            char tmp = (char) result[i];
            result[i] = tmp ^ rhs.charAt(7 - i);
        }
        // phase 3
        for (int i = 0; i < 4; i++) {
            char front = (char) result[i];
            char back = (char) result[3 - i];
            result[i] = front ^ back;
        }
        // final output
        for (int i = 0; i < 4; i++) {
            String c = String.format("%01X", result[i] % 16);
            out += c;
        }
        return out;
    }

    private static String[] getBlocks(String toHash) {
        int parts = toHash.length() / 8;
        String[] out = new String[parts];
        for (int i = 0; i < parts; i++) {
            out[i] = toHash.substring(i * 8, (i * 8) + 8);
        }
        return out;
    }
}