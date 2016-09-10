package business.logging;

import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class EbazSimpleFormatter extends SimpleFormatter {
	@Override
	public String format(LogRecord record) {
		String retVal = record.getLevel().toString()+": " +
			record.getMessage() + "\r\n";
		return retVal;
	}
}
