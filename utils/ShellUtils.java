private void execCommandEasily(String cmd, boolean printResult) throws Exception {
        JLog.d(TAG, "execCommandEasily() "+ cmd);
        Process p = Runtime.getRuntime().exec(cmd);
        p.waitFor();
        if (printResult) {
            printResult(p.getInputStream(), p.getErrorStream());
        }
    }

private void printResult(InputStream input, InputStream error) throws Exception {
	BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	for (String line; (line = reader.readLine()) != null; ) {
		if (line != null) {
			JLog.d(TAG, "consumeResult() input "+ line);
		}
	}

	reader = new BufferedReader(new InputStreamReader(error));
	for (String line; (line = reader.readLine()) != null; ) {
		if (line != null) {
			JLog.d(TAG, "consumeResult() error "+ line);
		}
	}
}
