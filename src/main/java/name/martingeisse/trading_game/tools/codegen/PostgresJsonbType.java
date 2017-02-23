package name.martingeisse.trading_game.tools.codegen;

import com.querydsl.sql.types.AbstractType;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 *
 */
public class PostgresJsonbType extends AbstractType<PostgresJsonb> {

	public PostgresJsonbType() {
		super(Types.VARCHAR);
	}

	@Override
	public Class<PostgresJsonb> getReturnedClass() {
		return PostgresJsonb.class;
	}

	@Nullable
	@Override
	public PostgresJsonb getValue(ResultSet rs, int startIndex) throws SQLException {
		return new PostgresJsonb(rs.getString(startIndex));
	}

	@Override
	public void setValue(PreparedStatement st, int startIndex, PostgresJsonb value) throws SQLException {
		st.setString(startIndex, value.getValue());
	}

}
