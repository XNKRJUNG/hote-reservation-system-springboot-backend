package edu.miu.cs.cs544.integration.logging;

import org.springframework.stereotype.Service;

@Service
public class LoggerImpl implements Logger {

	public void log(String logString) {
		java.util.logging.Logger.getLogger("ReservationLogger").info(logString);
	}

}
