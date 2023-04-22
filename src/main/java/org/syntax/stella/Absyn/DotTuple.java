// File generated by the BNF Converter (bnfc 2.9.4.1).

package org.syntax.stella.Absyn;

public class DotTuple  extends Expr {
  public final Expr expr_;
  public final Integer integer_;
  public int line_num, col_num, offset;
  public DotTuple(Expr p1, Integer p2) { expr_ = p1; integer_ = p2; }

  public <R,A> R accept(org.syntax.stella.Absyn.Expr.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(java.lang.Object o) {
    if (this == o) return true;
    if (o instanceof org.syntax.stella.Absyn.DotTuple) {
      org.syntax.stella.Absyn.DotTuple x = (org.syntax.stella.Absyn.DotTuple)o;
      return this.expr_.equals(x.expr_) && this.integer_.equals(x.integer_);
    }
    return false;
  }

  public int hashCode() {
    return 37*(this.expr_.hashCode())+this.integer_.hashCode();
  }


}
