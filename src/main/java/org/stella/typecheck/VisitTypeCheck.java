// File generated by the BNF Converter (bnfc 2.9.4.1).

package org.stella.typecheck;


import org.antlr.v4.runtime.misc.Pair;
import org.syntax.stella.Absyn.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*** Visitor Design Pattern Skeleton. ***/

/* This implements the common visitor design pattern.
   Tests show it to be slightly less efficient than the
   instanceof method, but easier to use.
   Replace the R and A parameters with the desired return
   and context types.*/

public class VisitTypeCheck
{
  public Map<String, ArrayList<String>> functionParametersDictionary = new HashMap<>();
  public Map<String, ArrayList<String>> variableParametersDictionary = new HashMap<>();
  public Map<String, Pair<ArrayList<String>, ArrayList<String>>> functions = new HashMap<>();
  public ArrayList<String> parameters = new ArrayList<>();

  public class ProgramVisitor<R,A> implements org.syntax.stella.Absyn.Program.Visitor<R,A>
  {
    public R visit(org.syntax.stella.Absyn.AProgram p, A arg)
    { /* Code for AProgram goes here */
      p.languagedecl_.accept(new LanguageDeclVisitor<R,A>(), arg);
      for (org.syntax.stella.Absyn.Extension x: p.listextension_) {
        x.accept(new ExtensionVisitor<R,A>(), arg);
      }
//      Add iszero to functions
      ArrayList<String> listParamToAdd = new ArrayList<>();
      listParamToAdd.add("TypeNat");
      ArrayList<String> listReturnToAdd = new ArrayList<>();
      listReturnToAdd.add("TypeBool");
      functions.put("iszero", new Pair<>(listParamToAdd, listReturnToAdd));
      for (org.syntax.stella.Absyn.Decl x: p.listdecl_) {
        x.accept(new DeclVisitor<R,A>(), arg);
        functionParametersDictionary.clear();
        variableParametersDictionary.clear();
        parameters.clear();
      }
      return null;
    }
  }
  public class LanguageDeclVisitor<R,A> implements org.syntax.stella.Absyn.LanguageDecl.Visitor<R,A>
  {
    public R visit(org.syntax.stella.Absyn.LanguageCore p, A arg)
    { /* Code for LanguageCore goes here */
      return null;
    }
  }
  public class ExtensionVisitor<R,A> implements org.syntax.stella.Absyn.Extension.Visitor<R,A>
  {
    public R visit(org.syntax.stella.Absyn.AnExtension p, A arg)
    { /* Code for AnExtension goes here */
      for (String x: p.listextensionname_) {
        //x;
      }
      return null;
    }
  }
  public class DeclVisitor<R,A> implements org.syntax.stella.Absyn.Decl.Visitor<R,A>
  {
    public R visit(org.syntax.stella.Absyn.DeclFun p, A arg)
    { /* Code for DeclFun goes here */
      for (org.syntax.stella.Absyn.Annotation x: p.listannotation_) {
        x.accept(new AnnotationVisitor<R,A>(), arg);
      }
      //p.stellaident_;
      for (org.syntax.stella.Absyn.ParamDecl x: p.listparamdecl_) {
        x.accept(new ParamDeclVisitor<R,A>(), arg);
      }
      ArrayList<String> expectetReturnType = p.returntype_.accept(new ReturnTypeVisitor<R,A>(), arg);
      p.throwtype_.accept(new ThrowTypeVisitor<R,A>(), arg);
      for (org.syntax.stella.Absyn.Decl x: p.listdecl_) {
        x.accept(new DeclVisitor<R,A>(), arg);
      }
      ArrayList<String> params = new ArrayList<>();
      params.addAll(parameters);
      Pair<ArrayList<String>, ArrayList<String>> pair = new Pair<>(params, expectetReturnType);
      functions.put(p.stellaident_, pair);
      ArrayList<String> actualReturnType = p.expr_.accept(new ExprVisitor<R,A>(), arg);
//      Compare return type of function
      if (!actualReturnType.equals(expectetReturnType)){
        System.out.println("Type Error");
        System.out.println("Return type mismatch in function " + p.stellaident_);
        System.exit(1);
      }
      return null;
    }

    public R visit(org.syntax.stella.Absyn.DeclTypeAlias p, A arg)
    { /* Code for DeclTypeAlias goes here */
      //p.stellaident_;
      p.type_.accept(new TypeVisitor<R,A>(), arg);
      return null;
    }

    public R visit(DeclExceptionType p, A arg) {
      return null;
    }

    public R visit(DeclExceptionVariant p, A arg) {
      return null;
    }
  }
  public class LocalDeclVisitor<R,A> implements org.syntax.stella.Absyn.LocalDecl.Visitor<R,A>
  {
    public R visit(org.syntax.stella.Absyn.ALocalDecl p, A arg)
    { /* Code for ALocalDecl goes here */
      p.decl_.accept(new DeclVisitor<R,A>(), arg);
      return null;
    }
  }
  public class AnnotationVisitor<R,A> implements org.syntax.stella.Absyn.Annotation.Visitor<R,A>
  {
    public R visit(org.syntax.stella.Absyn.InlineAnnotation p, A arg)
    { /* Code for InlineAnnotation goes here */
      return null;
    }
  }
  public class ParamDeclVisitor<R,A> implements org.syntax.stella.Absyn.ParamDecl.Visitor<R,A>
  {
    public R visit(org.syntax.stella.Absyn.AParamDecl p, A arg)
    { /* Code for AParamDecl goes here */
      //p.stellaident_;
//      Check if the parameter already exists then add it to functions or to the variables parameters
      ArrayList<String> type = p.type_.accept(new TypeVisitor<R,A>(), arg);
      if (variableParametersDictionary.containsKey(p.stellaident_) ||
              functionParametersDictionary.containsKey(p.stellaident_)){
        System.out.println("The variable " + p.stellaident_ + " is already defined");
        System.exit(1);
      }
      if (type.size() == 1 || p.type_ instanceof TypeTuple || p.type_ instanceof TypeSum ||
              p.type_ instanceof TypeRecord || p.type_ instanceof TypeRef){
        variableParametersDictionary.put(p.stellaident_, type);
      } else {
        functionParametersDictionary.put(p.stellaident_, type);
      }
      parameters.addAll(type);
      return null;
    }
  }
  public class ReturnTypeVisitor<R,A> implements org.syntax.stella.Absyn.ReturnType.Visitor<ArrayList<String>,A>
  {
    public ArrayList<String> visit(org.syntax.stella.Absyn.NoReturnType p, A arg)
    { /* Code for NoReturnType goes here */
      System.out.println("Type Error");
      System.out.println("The function should have return statement");
      System.exit(1);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.SomeReturnType p, A arg)
    { /* Code for SomeReturnType goes here */
      return p.type_.accept(new TypeVisitor<R,A>(), arg);
    }
  }
  public class ThrowTypeVisitor<R,A> implements org.syntax.stella.Absyn.ThrowType.Visitor<R,A>
  {
    public R visit(org.syntax.stella.Absyn.NoThrowType p, A arg)
    { /* Code for NoThrowType goes here */
      return null;
    }
    public R visit(org.syntax.stella.Absyn.SomeThrowType p, A arg)
    { /* Code for SomeThrowType goes here */
      for (org.syntax.stella.Absyn.Type x: p.listtype_) {
        x.accept(new TypeVisitor<R,A>(), arg);
      }
      return null;
    }
  }
  public class TypeVisitor<R,A> implements org.syntax.stella.Absyn.Type.Visitor<ArrayList<String>,A>
  {
    public ArrayList<String> visit(org.syntax.stella.Absyn.TypeFun p, A arg)
    { /* Code for TypeFun goes here */
//      Determine the type of a function parameters and return
      ArrayList<String> result = new ArrayList<>();
      result.add("Fun");
      for (org.syntax.stella.Absyn.Type x: p.listtype_) {
        result.addAll(x.accept(new TypeVisitor<R,A>(), arg));
      }
      result.addAll(p.type_.accept(new TypeVisitor<R,A>(), arg));
      return result;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.TypeRec p, A arg)
    { /* Code for TypeRec goes here */
      //p.stellaident_;
      p.type_.accept(new TypeVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.TypeSum p, A arg)
    { /* Code for TypeSum goes here */
//      Determine the type of a sum
      ArrayList<String> sum = new ArrayList<>();
      sum.add("Sum");
      sum.addAll(p.type_1.accept(new TypeVisitor<R,A>(), arg));
      sum.addAll(p.type_2.accept(new TypeVisitor<R,A>(), arg));
      return sum;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.TypeTuple p, A arg)
    { /* Code for TypeTuple goes here */
//      Determine the type of a pair
      ArrayList<String> tupleType = new ArrayList<>();
      tupleType.add("Pair");
      for (org.syntax.stella.Absyn.Type x: p.listtype_) {
        tupleType.addAll(x.accept(new TypeVisitor<R,A>(), arg));
      }
      return tupleType;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.TypeRecord p, A arg)
    { /* Code for TypeRecord goes here */
      ArrayList<String> result = new ArrayList<>();
      result.add("Record");
      for (org.syntax.stella.Absyn.RecordFieldType x: p.listrecordfieldtype_) {
        result.add(((ARecordFieldType) x).stellaident_);
        result.addAll(x.accept(new RecordFieldTypeVisitor<R,A>(), arg));
      }
      return result;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.TypeVariant p, A arg)
    { /* Code for TypeVariant goes here */
      for (org.syntax.stella.Absyn.VariantFieldType x: p.listvariantfieldtype_) {
        x.accept(new VariantFieldTypeVisitor<R,A>(), arg);
      }
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.TypeList p, A arg)
    { /* Code for TypeList goes here */
      p.type_.accept(new TypeVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.TypeBool p, A arg)
    { /* Code for TypeBool goes here */
      ArrayList<String> result = new ArrayList<>();
      result.add("TypeBool");
      return result;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.TypeNat p, A arg)
    { /* Code for TypeNat goes here */
      ArrayList<String> result = new ArrayList<>();
      result.add("TypeNat");
      return result;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.TypeUnit p, A arg)
    { /* Code for TypeUnit goes here */
      ArrayList<String> result = new ArrayList<>();
      result.add("TypeUnit");
      return result;
    }

    public ArrayList<String> visit(TypeTop p, A arg) {
      return null;
    }

    public ArrayList<String> visit(TypeBottom p, A arg) {
      return null;
    }

    public ArrayList<String> visit(TypeRef p, A arg) {
      ArrayList<String> result = new ArrayList<>();
      result.add("Ref");
      result.addAll(p.type_.accept(new TypeVisitor<R,A>(), arg));
      return result;
    }

    public ArrayList<String> visit(org.syntax.stella.Absyn.TypeVar p, A arg)
    { /* Code for TypeVar goes here */
      //p.stellaident_;
      return null;
    }
  }
  public class MatchCaseVisitor<R,A> implements org.syntax.stella.Absyn.MatchCase.Visitor<R,A>
  {
    public R visit(org.syntax.stella.Absyn.AMatchCase p, A arg)
    { /* Code for AMatchCase goes here */
      p.pattern_.accept(new PatternVisitor<R,A>(), arg);
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
  }
  public class OptionalTypingVisitor<R,A> implements org.syntax.stella.Absyn.OptionalTyping.Visitor<R,A>
  {
    public R visit(org.syntax.stella.Absyn.NoTyping p, A arg)
    { /* Code for NoTyping goes here */
      return null;
    }
    public R visit(org.syntax.stella.Absyn.SomeTyping p, A arg)
    { /* Code for SomeTyping goes here */
      p.type_.accept(new TypeVisitor<R,A>(), arg);
      return null;
    }
  }
  public class PatternDataVisitor<R,A> implements org.syntax.stella.Absyn.PatternData.Visitor<R,A>
  {
    public R visit(org.syntax.stella.Absyn.NoPatternData p, A arg)
    { /* Code for NoPatternData goes here */
      return null;
    }
    public R visit(org.syntax.stella.Absyn.SomePatternData p, A arg)
    { /* Code for SomePatternData goes here */
      p.pattern_.accept(new PatternVisitor<R,A>(), arg);
      return null;
    }
  }
  public class ExprDataVisitor<R,A> implements org.syntax.stella.Absyn.ExprData.Visitor<R,A>
  {
    public R visit(org.syntax.stella.Absyn.NoExprData p, A arg)
    { /* Code for NoExprData goes here */
      return null;
    }
    public R visit(org.syntax.stella.Absyn.SomeExprData p, A arg)
    { /* Code for SomeExprData goes here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
  }
  public class PatternVisitor<R,A> implements org.syntax.stella.Absyn.Pattern.Visitor<R,A>
  {
    public R visit(org.syntax.stella.Absyn.PatternVariant p, A arg)
    { /* Code for PatternVariant goes here */
      //p.stellaident_;
      p.patterndata_.accept(new PatternDataVisitor<R,A>(), arg);
      return null;
    }
    public R visit(org.syntax.stella.Absyn.PatternInl p, A arg)
    { /* Code for PatternInl goes here */
      p.pattern_.accept(new PatternVisitor<R,A>(), arg);
      return null;
    }
    public R visit(org.syntax.stella.Absyn.PatternInr p, A arg)
    { /* Code for PatternInr goes here */
      p.pattern_.accept(new PatternVisitor<R,A>(), arg);
      return null;
    }
    public R visit(org.syntax.stella.Absyn.PatternTuple p, A arg)
    { /* Code for PatternTuple goes here */
      for (org.syntax.stella.Absyn.Pattern x: p.listpattern_) {
        x.accept(new PatternVisitor<R,A>(), arg);
      }
      return null;
    }
    public R visit(org.syntax.stella.Absyn.PatternRecord p, A arg)
    { /* Code for PatternRecord goes here */
      for (org.syntax.stella.Absyn.LabelledPattern x: p.listlabelledpattern_) {
        x.accept(new LabelledPatternVisitor<R,A>(), arg);
      }
      return null;
    }
    public R visit(org.syntax.stella.Absyn.PatternList p, A arg)
    { /* Code for PatternList goes here */
      for (org.syntax.stella.Absyn.Pattern x: p.listpattern_) {
        x.accept(new PatternVisitor<R,A>(), arg);
      }
      return null;
    }
    public R visit(org.syntax.stella.Absyn.PatternCons p, A arg)
    { /* Code for PatternCons goes here */
      p.pattern_1.accept(new PatternVisitor<R,A>(), arg);
      p.pattern_2.accept(new PatternVisitor<R,A>(), arg);
      return null;
    }
    public R visit(org.syntax.stella.Absyn.PatternFalse p, A arg)
    { /* Code for PatternFalse goes here */
      return null;
    }
    public R visit(org.syntax.stella.Absyn.PatternTrue p, A arg)
    { /* Code for PatternTrue goes here */
      return null;
    }
    public R visit(org.syntax.stella.Absyn.PatternUnit p, A arg)
    { /* Code for PatternUnit goes here */
      return null;
    }
    public R visit(org.syntax.stella.Absyn.PatternInt p, A arg)
    { /* Code for PatternInt goes here */
      //p.integer_;
      return null;
    }
    public R visit(org.syntax.stella.Absyn.PatternSucc p, A arg)
    { /* Code for PatternSucc goes here */
      p.pattern_.accept(new PatternVisitor<R,A>(), arg);
      return null;
    }
    public R visit(org.syntax.stella.Absyn.PatternVar p, A arg)
    { /* Code for PatternVar goes here */
      //p.stellaident_;
      return null;
    }
  }
  public class LabelledPatternVisitor<R,A> implements org.syntax.stella.Absyn.LabelledPattern.Visitor<R,A>
  {
    public R visit(org.syntax.stella.Absyn.ALabelledPattern p, A arg)
    { /* Code for ALabelledPattern goes here */
      //p.stellaident_;
      p.pattern_.accept(new PatternVisitor<R,A>(), arg);
      return null;
    }
  }
  public class BindingVisitor<R,A> implements org.syntax.stella.Absyn.Binding.Visitor<ArrayList<String>,A>
  {
    public ArrayList<String> visit(org.syntax.stella.Absyn.ABinding p, A arg)
    { /* Code for ABinding goes here */
      //p.stellaident_;
      ArrayList<String> result = new ArrayList<>();
      result.add(p.stellaident_);
      result.addAll(p.expr_.accept(new ExprVisitor<R,A>(), arg));
      return result;
    }
  }
  public class ExprVisitor<R,A> implements org.syntax.stella.Absyn.Expr.Visitor<ArrayList<String>,A>
  {
    public ArrayList<String> visit(org.syntax.stella.Absyn.Sequence p, A arg)
    { /* Code for Sequence goes here */
      ArrayList<String> listToCompare = new ArrayList<>();
      listToCompare.add("TypeUnit");
      ArrayList<String> typeExpr1 = p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      if (!listToCompare.equals(typeExpr1)){
        System.out.println("Type Error");
        System.out.println("The expression in sequence should have Type Unit");
        System.exit(1);
      }
      return p.expr_2.accept(new ExprVisitor<R,A>(), arg);
    }


    public ArrayList<String> visit(Assign p, A arg) {
      ArrayList<String> result = new ArrayList<>();
      result.add("TypeUnit");
      ArrayList<String> expectedType = p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      ArrayList<String> actualType = p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      if (!expectedType.subList(1, expectedType.size()).equals(actualType)){
        System.out.println("TypeError");
        System.out.println("The wrong type of value for the variable " + ((Var) p.expr_1).stellaident_);
        System.exit(1);
      }
      return result;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.If p, A arg)
    { /* Code for If goes here */
//      Check that the condition under if type bool
      ArrayList<String> listToCompare = new ArrayList<>();
      if (p.expr_1 instanceof Var){
        String varName = ((Var) p.expr_1).stellaident_;
        if (!variableParametersDictionary.containsKey(varName)){
          System.out.println("Type Error");
          System.out.println("Variable " + varName + " is undefined");
          System.exit(1);
        } else if (!(variableParametersDictionary.get(varName).get(0).equals("TypeBool"))) {
          System.out.println("Type Error");
          System.out.println("Expression under If should have TypeBool");
          System.exit(1);
        }
      }
      listToCompare.add("TypeBool");
      if (!(p.expr_1.accept(new ExprVisitor<R,A>(), arg).equals(listToCompare))){
        System.out.println("Type Error");
        System.out.println("Expression under If should have TypeBool");
        System.exit(1);
      }

//      Check that expressions under then and else have the same type
      if (!(p.expr_2 instanceof Var || p.expr_3 instanceof Var)){
        if (p.expr_2.accept(new ExprVisitor<R,A>(), arg).equals(p.expr_3.accept(new ExprVisitor<R,A>(), arg))){
          return p.expr_2.accept(new ExprVisitor<R,A>(), arg);
        } else{
          System.out.println("Type Error");
          System.out.println("Expression under then and else should have the same type");
          System.exit(1);
        }
      } else if (p.expr_2 instanceof Var){
        String varName2 = ((Var) p.expr_2).stellaident_;
        if (!variableParametersDictionary.containsKey(varName2)){
          System.out.println("Type Error");
          System.out.println("Variable " + varName2 + " is undefined");
          System.exit(1);
        }
        if (p.expr_3 instanceof Var){
          String varName3 = ((Var) p.expr_3).stellaident_;
          if (!variableParametersDictionary.containsKey(varName3)){
            System.out.println("Type Error");
            System.out.println("Variable " + varName3 + " is undefined");
            System.exit(1);
          }
          if (!(variableParametersDictionary.get(varName2).
                  equals(variableParametersDictionary.get(varName3)))){
            System.out.println("Type Error");
            System.out.println("Expression under then and else should have the same type");
            System.exit(1);
          }
          listToCompare.clear();
          listToCompare.addAll(variableParametersDictionary.get(varName2));
          return listToCompare;
        }
        else{
          listToCompare.clear();
          listToCompare.addAll(variableParametersDictionary.get(varName2));
          if (!(listToCompare.equals(p.expr_3.accept(new ExprVisitor<R,A>(), arg)))){
            System.out.println("Type Error");
            System.out.println("Expression under then and else should have the same type");
            System.exit(1);
          }
          return listToCompare;
        }
      } else {
        String varName3 = ((Var) p.expr_3).stellaident_;
        if (!variableParametersDictionary.containsKey(varName3)){
          System.out.println("Type Error");
          System.out.println("Variable " + varName3 + " is undefined");
          System.exit(1);
        }
        listToCompare.clear();
        listToCompare.addAll(variableParametersDictionary.get(varName3));
        if (!(listToCompare.equals(p.expr_2.accept(new ExprVisitor<R,A>(), arg)))){
          System.out.println("Type Error");
          System.out.println("Expression under then and else should have the same type");
          System.exit(1);
        }
        return listToCompare;
      }
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      p.expr_3.accept(new ExprVisitor<R,A>(), arg);
      return p.expr_2.accept(new ExprVisitor<R,A>(), arg);
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.Let p, A arg)
    { /* Code for Let goes here */
      for (org.syntax.stella.Absyn.PatternBinding x: p.listpatternbinding_) {
        x.accept(new PatternBindingVisitor<R,A>(), arg);
      }
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.LetRec p, A arg)
    { /* Code for LetRec goes here */
      for (org.syntax.stella.Absyn.PatternBinding x: p.listpatternbinding_) {
        x.accept(new PatternBindingVisitor<R,A>(), arg);
      }
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.LessThan p, A arg)
    { /* Code for LessThan goes here */
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.LessThanOrEqual p, A arg)
    { /* Code for LessThanOrEqual goes here */
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.GreaterThan p, A arg)
    { /* Code for GreaterThan goes here */
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.GreaterThanOrEqual p, A arg)
    { /* Code for GreaterThanOrEqual goes here */
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.Equal p, A arg)
    { /* Code for Equal goes here */
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.NotEqual p, A arg)
    { /* Code for NotEqual goes here */
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.TypeAsc p, A arg)
    { /* Code for TypeAsc goes here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      p.type_.accept(new TypeVisitor<R,A>(), arg);
      return null;
    }

    public ArrayList<String> visit(TypeCast p, A arg) {
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.Abstraction p, A arg)
    { /* Code for Abstraction goes here */
      for (org.syntax.stella.Absyn.ParamDecl x: p.listparamdecl_) {
        x.accept(new ParamDeclVisitor<R,A>(), arg);
      }
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.Variant p, A arg)
    { /* Code for Variant goes here */
      //p.stellaident_;
      p.exprdata_.accept(new ExprDataVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.Match p, A arg)
    { /* Code for Match goes here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      for (org.syntax.stella.Absyn.MatchCase x: p.listmatchcase_) {
        x.accept(new MatchCaseVisitor<R,A>(), arg);
      }
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.List p, A arg)
    { /* Code for List goes here */
      for (org.syntax.stella.Absyn.Expr x: p.listexpr_) {
        x.accept(new ExprVisitor<R,A>(), arg);
      }
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.Add p, A arg)
    { /* Code for Add goes here */
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.Subtract p, A arg)
    { /* Code for Subtract goes here */
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.LogicOr p, A arg)
    { /* Code for LogicOr goes here */
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.Multiply p, A arg)
    { /* Code for Multiply goes here */
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.Divide p, A arg)
    { /* Code for Divide goes here */
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.LogicAnd p, A arg)
    { /* Code for LogicAnd goes here */
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }

    public ArrayList<String> visit(Ref p, A arg) {
      ArrayList<String> result = new ArrayList<>();
      result.add("Ref");
      result.addAll(p.expr_.accept(new ExprVisitor<R,A>(), arg));
      return result;
    }

    public ArrayList<String> visit(Deref p, A arg) {
      if (p.expr_ instanceof Var){
        String varName = ((Var) p.expr_).stellaident_;
        if (!variableParametersDictionary.containsKey(varName)){
          System.out.println("Type Error");
          System.out.println("The variable " + varName + " is undefined");
          System.exit(1);
        } else if (!variableParametersDictionary.get(varName).get(0).equals("Ref")){
          System.out.println("Type Error");
          System.out.println("Cannot dereference variable " + varName);
          System.exit(1);
        } else {
          ArrayList<String> result = new ArrayList<>();
          result.addAll(variableParametersDictionary.get(varName).subList(1, variableParametersDictionary.get(varName).size()));
          return result;
        }
      }
      System.out.println("Type Error");
      System.out.println("Cannot dereference expression");
      System.exit(1);
      return null;
    }

    public ArrayList<String> visit(org.syntax.stella.Absyn.Application p, A arg)
    { /* Code for Application goes here */
//      Check that application is a function and compares arguments
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      ArrayList<String> type = new ArrayList<>();
      for (org.syntax.stella.Absyn.Expr x: p.listexpr_) {
        type = x.accept(new ExprVisitor<R,A>(), arg);
      }
      if (p.expr_ instanceof Var){
        String varName = ((Var) p.expr_).stellaident_;
        if (!(functionParametersDictionary.containsKey(varName) || functions.containsKey(varName))) {
          System.out.println("Type Error");
          System.out.println("Applying non-function");
          System.exit(1);
        }
        if (functions.containsKey(varName)) {
          if (!functions.get(varName).a.equals(type)) {
            System.out.println("Type Error");
            System.out.println("Wrong parameters for function " + varName);
            System.exit(1);
          }
          return functions.get(varName).b;
        }
      }
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.DotRecord p, A arg)
    { /* Code for DotRecord goes here */
      ArrayList<String> helpList = new ArrayList<>();
      ArrayList<String> result = new ArrayList<>();
      helpList.addAll(p.expr_.accept(new ExprVisitor<R,A>(), arg));
      int idx = helpList.indexOf(p.stellaident_);
      if (idx != -1){
        result.add(helpList.get(idx + 1));
      } else {
        System.out.println("Type Error");
        System.out.println("The field " + p.stellaident_ + " doesn't exist");
        System.exit(1);
      }
      //p.stellaident_;
      return result;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.DotTuple p, A arg)
    { /* Code for DotTuple goes here */
//      returns the type of the element and check that argument is a pair
      ArrayList<String> result = new ArrayList<>();
      ArrayList<String> listToCompare = new ArrayList<>();
      listToCompare.addAll(p.expr_.accept(new ExprVisitor<R,A>(), arg));
      if (!listToCompare.get(0).equals("Pair")){
        System.out.println("Type Error");
        System.out.println("Can not access element of pair");
        System.exit(1);
      }
      result.add(listToCompare.get(p.integer_));
      return result;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.Tuple p, A arg)
    { /* Code for Tuple goes here */
//      Determine the type of a pair
      ArrayList<String> pair = new ArrayList<>();
      pair.add("Pair");
      for (org.syntax.stella.Absyn.Expr x: p.listexpr_) {
        pair.addAll(x.accept(new ExprVisitor<R,A>(), arg));
      }
      return pair;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.Record p, A arg)
    { /* Code for Record goes here */
      ArrayList<String> result = new ArrayList<>();
      result.add("Record");
      for (org.syntax.stella.Absyn.Binding x: p.listbinding_) {
        result.addAll(x.accept(new BindingVisitor<R,A>(), arg));
      }
      return result;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.ConsList p, A arg)
    { /* Code for ConsList goes here */
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.Head p, A arg)
    { /* Code for Head goes here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.IsEmpty p, A arg)
    { /* Code for IsEmpty goes here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.Tail p, A arg)
    { /* Code for Tail goes here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }

    public ArrayList<String> visit(Panic p, A arg) {
      return null;
    }

    public ArrayList<String> visit(Throw p, A arg) {
      return null;
    }

    public ArrayList<String> visit(TryCatch p, A arg) {
      return null;
    }

    public ArrayList<String> visit(TryWith p, A arg) {
      return null;
    }


    public ArrayList<String> visit(org.syntax.stella.Absyn.Inl p, A arg)
    { /* Code for Inl goes here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.Inr p, A arg)
    { /* Code for Inr goes here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.Succ p, A arg)
    { /* Code for Succ goes here */
//      Check that the expression under succ has typeNat
      if (p.expr_ instanceof Abstraction){
        System.out.println("Type Error");
        System.out.println("Expression under succ shouldn't be anonymous function");
        System.exit(1);
      }
      if (p.expr_ instanceof Var){
        String varName = ((Var) p.expr_).stellaident_;
        if (!variableParametersDictionary.containsKey(varName)){
          System.out.println("Type Error");
          System.out.println("Variable " + varName + "is undefined");
          System.exit(1);
        } else if (!(variableParametersDictionary.get(varName).get(0).equals("TypeNat"))) {
          System.out.println("Type Error");
          System.out.println("Expression under Succ should have TypeNat");
          System.exit(1);
        }
      }
      ArrayList<String> listToCompare = new ArrayList<>();
      listToCompare.add("TypeNat");
      if (!(p.expr_.accept(new ExprVisitor<R,A>(), arg).equals(listToCompare))){
        System.out.println("Type Error");
        System.out.println("Expression under Succ should have TypeNat");
        System.exit(1);
      }
      return listToCompare;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.LogicNot p, A arg)
    { /* Code for LogicNot goes here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.Pred p, A arg)
    { /* Code for Pred goes here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.IsZero p, A arg)
    { /* Code for IsZero goes here */
//      Check that the expression under iszero has typeNat
      if (p.expr_ instanceof Var){
        String varName = ((Var) p.expr_).stellaident_;
        if (!variableParametersDictionary.containsKey(varName)){
          System.out.println("Type Error");
          System.out.println("Variable " + varName + "is undefined");
          System.exit(1);
        } else if (!(variableParametersDictionary.get(varName).get(0).equals("TypeNat"))) {
          System.out.println("Type Error");
          System.out.println("Expression under IsZero should have TypeNat");
          System.exit(1);
        }
      }
      ArrayList<String> listToCompare = new ArrayList<>();
      listToCompare.add("TypeNat");
      if (!(p.expr_.accept(new ExprVisitor<R,A>(), arg).equals(listToCompare))){
        System.out.println("Type Error");
        System.out.println("Expression under IsZero should have TypeNat");
        System.exit(1);
      }
      listToCompare.clear();
      listToCompare.add("TypeBool");
      return listToCompare;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.Fix p, A arg)
    { /* Code for Fix goes here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.NatRec p, A arg)
    { /* Code for NatRec goes here */
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      p.expr_3.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.Fold p, A arg)
    { /* Code for Fold goes here */
      p.type_.accept(new TypeVisitor<R,A>(), arg);
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.Unfold p, A arg)
    { /* Code for Unfold goes here */
      p.type_.accept(new TypeVisitor<R,A>(), arg);
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.ConstTrue p, A arg)
    { /* Code for ConstTrue goes here */
      ArrayList<String> result = new ArrayList<>();
      result.add("TypeBool");
      return result;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.ConstFalse p, A arg)
    { /* Code for ConstFalse goes here */
      ArrayList<String> result = new ArrayList<>();
      result.add("TypeBool");
      return result;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.ConstUnit p, A arg)
    { /* Code for ConstUnit goes here */
      ArrayList<String> result = new ArrayList<>();
      result.add("TypeUnit");
      return result;
    }
    public ArrayList<String> visit(org.syntax.stella.Absyn.ConstInt p, A arg)
    {
      ArrayList<String> result = new ArrayList<>();
      result.add("TypeNat");
      return result;
    }

    public ArrayList<String> visit(ConstMemory p, A arg) {
      return null;
    }

    public ArrayList<String> visit(org.syntax.stella.Absyn.Var p, A arg)
    { /* Code for Var goes here */
      //p.stellaident_;
//      Checks if the variable exists and returns its type
      ArrayList<String> result = new ArrayList<>();
      if (variableParametersDictionary.containsKey(p.stellaident_)){
        result.addAll(variableParametersDictionary.get(p.stellaident_));
      } else if (functionParametersDictionary.containsKey(p.stellaident_))
        result.addAll(functionParametersDictionary.get(p.stellaident_));
      else if (!functions.containsKey(p.stellaident_)){
        System.out.println("Type Error");
        System.out.println("The variable " + p.stellaident_ + " is undefined");
        System.exit(1);
      }
      return result;
    }
  }
  public class PatternBindingVisitor<R,A> implements org.syntax.stella.Absyn.PatternBinding.Visitor<R,A>
  {
    public R visit(org.syntax.stella.Absyn.APatternBinding p, A arg)
    { /* Code for APatternBinding goes here */
      p.pattern_.accept(new PatternVisitor<R,A>(), arg);
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
  }
  public class VariantFieldTypeVisitor<R,A> implements org.syntax.stella.Absyn.VariantFieldType.Visitor<R,A>
  {
    public R visit(org.syntax.stella.Absyn.AVariantFieldType p, A arg)
    { /* Code for AVariantFieldType goes here */
      //p.stellaident_;
      p.optionaltyping_.accept(new OptionalTypingVisitor<R,A>(), arg);
      return null;
    }
  }
  public class RecordFieldTypeVisitor<R,A> implements org.syntax.stella.Absyn.RecordFieldType.Visitor<ArrayList<String>,A>
  {
    public ArrayList<String> visit(org.syntax.stella.Absyn.ARecordFieldType p, A arg)
    { /* Code for ARecordFieldType goes here */
      //p.stellaident_;
      ArrayList<String> result = new ArrayList<>();
      result.addAll(p.type_.accept(new TypeVisitor<R,A>(), arg));
      return result;
    }
  }
  public class TypingVisitor<R,A> implements org.syntax.stella.Absyn.Typing.Visitor<R,A>
  {
    public R visit(org.syntax.stella.Absyn.ATyping p, A arg)
    { /* Code for ATyping goes here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      p.type_.accept(new TypeVisitor<R,A>(), arg);
      return null;
    }
  }
}
