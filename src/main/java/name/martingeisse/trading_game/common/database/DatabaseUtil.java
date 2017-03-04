package name.martingeisse.trading_game.common.database;

import com.querydsl.core.QueryException;
import org.postgresql.util.PSQLException;

/**
 *
 */
public class DatabaseUtil {

	public static boolean isDuplicateKeyViolation(QueryException e) {
		Throwable cause = e.getCause();
		return (cause instanceof PSQLException) && isDuplicateKeyViolation((PSQLException) cause);
	}

	public static boolean isDuplicateKeyViolation(PSQLException e) {
		return (e.getMessage().startsWith("ERROR: duplicate key value violates unique constraint "));
	}

}
