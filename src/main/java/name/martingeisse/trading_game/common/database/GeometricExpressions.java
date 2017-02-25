package name.martingeisse.trading_game.common.database;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import org.postgresql.geometric.PGpoint;

/**
 * This class contains static factory methods for Postgres geometric expressions. Only euclidian geometry is needed for
 * this game, so it works in core Postgres (without PostGIS).
 */
public final class GeometricExpressions {

	/**
	 * Creates an expression that is true iff the x coordinate of point a is less than that of point b.
	 */
	public static BooleanExpression xLessThan(Expression<PGpoint> a, Expression<PGpoint> b) {
		return Expressions.booleanTemplate("(({0}) << ({1}))", a, b);
	}

	/**
	 * Creates an expression that is true iff the x coordinate of point a is greater than that of point b.
	 */
	public static BooleanExpression xGreaterThan(Expression<PGpoint> a, Expression<PGpoint> b) {
		return Expressions.booleanTemplate("(({0}) >> ({1}))", a, b);
	}

	/**
	 * Creates an expression that is true iff the y coordinate of point a is less than that of point b.
	 */
	public static BooleanExpression yLessThan(Expression<PGpoint> a, Expression<PGpoint> b) {
		return Expressions.booleanTemplate("(({0}) <^ ({1}))", a, b);
	}

	/**
	 * Creates an expression that is true iff the y coordinate of point a is greater than that of point b.
	 */
	public static BooleanExpression yGreaterThan(Expression<PGpoint> a, Expression<PGpoint> b) {
		return Expressions.booleanTemplate("(({0}) >^ ({1}))", a, b);
	}

	/**
	 * Creates an expression that is true iff point p lies within the rectangle defined by minPoint and maxPoint,
	 * assuming that both the x and y coordinate of minPoint are less than or equal to those of maxPoint.
	 * <p>
	 * Note that the minPoint and maxPoint expressions are used twice by the resulting expression.
	 */
	public static BooleanExpression pointInsideRectangle(Expression<PGpoint> p, Expression<PGpoint> minPoint, Expression<PGpoint> maxPoint) {
		BooleanExpression xmin = xGreaterThan(p, minPoint);
		BooleanExpression xmax = xLessThan(p, maxPoint);
		BooleanExpression ymin = yGreaterThan(p, minPoint);
		BooleanExpression ymax = yLessThan(p, maxPoint);
		return xmin.and(xmax).and(ymin).and(ymax);
	}


	/**
	 * Creates an expression that is true iff point p lies within the (square) bounding box with the specified center
	 * and "radius" (half the side length of the square).
	 */
	public static BooleanExpression pointInsideBoundingBox(Expression<PGpoint> p, double x, double y, double radius) {
		Expression<PGpoint> minPoint = Expressions.constant(new PGpoint(x - radius, y - radius));
		Expression<PGpoint> maxPoint = Expressions.constant(new PGpoint(x + radius, y + radius));
		return pointInsideRectangle(p, minPoint, maxPoint);
	}

}
