/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package XTR_Fast;

//import InMem.*;
//import explelan.*;
//import naiive.*;
//import XTR_Basic.*;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import metadata.Metadata;
import net.proteanit.sql.DbUtils;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

/**
 *
 * @author uqiibra2
 */
public class XTR_FastPredicate {
    String PredName="";
    String PredicateWehe="";
    double NoRecods=0;
    double ReductionFactor=0;
    double InternalSim=0;
    double TargetSim=Double.NaN;
    double ValueAfterDel=0;
    double Influence=0;
    // for 
    double PredSingleAggValue=Double.NaN;
   
   public DefaultTableModel PredAggData=new DefaultTableModel ();
    DefaultTableModel PredOrgData=new DefaultTableModel ();
    public XTR_FastView View;
    public Metadata M=null;
    public DefaultTableModel PredAtts=new DefaultTableModel ();
    //DefaultTableModel PredNumAtt=new DefaultTableModel ();
    
    public void ComputeInternalSimilirity(){
        
        DefaultTableModel PredicateOrgData=GetPredOrgData();
       // System.out.println("PredicateOrgValue \t CurrOrgValue");
        double utility = 0;
            for(int i=0;i<this.View.OrgData.getRowCount();i++){
                double resOrgvalue=Double.parseDouble( this.View.OrgData.getValueAt(i, 0).toString());
                double resPredcvalue=0;
                if (i<PredicateOrgData.getRowCount()) resPredcvalue=Double.parseDouble( PredicateOrgData.getValueAt(i, 0).toString());
                else  resPredcvalue=0;
                    //resPredcvalue=0;
                  // System.out.println(resPredcvalue+"\t"+resOrgvalue);
                    utility += Math.pow(resOrgvalue-resPredcvalue,2);
                
                
            }
            utility = Math.sqrt(utility);
            this.InternalSim=utility;
         // return utility;
    }
    
    // Mesaures Used for Computing the Objective Function 
    
    public void ComputeTragetSimilirity(){
        
        DefaultTableModel PredAggData=GetPredAggData();
        //System.out.println("PredicateAggValue \t TargetAggValue");
        double utility = 0;
            for(int i=0;i<this.View.lines.getRowCount();i++){
                double resTargetgvalue=Double.parseDouble( this.View.lines.getValueAt(i, 2).toString())/this.View.NormalizeV;
                double resRealgvalue=Double.parseDouble( this.View.lines.getValueAt(i, 1).toString())/this.View.NormalizeV;
                String ResId=this.View.lines.getValueAt(i, 0).toString();
                
                double resPredcvalue= GetResultID_Value(ResId );
               
                
                if (Double.isNaN(resPredcvalue)) {
                    resPredcvalue=0;
                    resPredcvalue=resRealgvalue;
                }
               // System.out.println(resPredcvalue+"\t"+resTargetgvalue);
                    utility += Math.pow(resTargetgvalue-resPredcvalue,2);
                
                
            }
            utility = Math.sqrt(utility);
             this.TargetSim=utility;
        //  return utility;
    }
    
    
    public double GetResultID_Value(String ResID){
        
        if(PredAggData.getRowCount()<1)
            this.PredAggData=GetPredAggData();
        //System.out.println("PredicateAggValue \t TargetAggValue");
        double vale =Double.NaN;
                        
                for(int j=0;j<this.PredAggData.getRowCount();j++){
                     String PredResId=this.PredAggData.getValueAt(j, 0).toString().replaceAll(" ", "");
                     if(PredResId.equals(ResID.replaceAll(" ", ""))){
                      vale=Double.parseDouble( this.PredAggData.getValueAt(j, 1).toString())/this.View.NormalizeV;
                      return vale;
                     }
                }
                
               
                
          
          return vale;
    }
    
      
    
    public void ComputeTragetPCorrDis(){
        
        DefaultTableModel PredAggData=GetPredAggData();
        //System.out.println("PredicateAggValue \t TargetAggValue");
        double PredAggDataMean=GetPredAggMean();
         double diffVline=0;
         double diffPredv=0;
         double Cov=0;
        double StdVline = 0;
        double StdPred = 0;
            for(int i=0;i<this.View.lines.getRowCount();i++){
                double VLine=Double.parseDouble( this.View.lines.getValueAt(i, 2).toString())/this.View.NormalizeV;
                double resPredcvalue=0;
                if (i<PredAggData.getRowCount()) resPredcvalue=Double.parseDouble( PredAggData.getValueAt(i, 1).toString());
                else  resPredcvalue=0;
               // System.out.println(resPredcvalue+"\t"+resTargetgvalue);
               diffVline=VLine-this.View.TargetMean;
               diffPredv=resPredcvalue-PredAggDataMean;
               Cov=Cov+ (diffVline*diffPredv);
                  StdVline=StdVline+Math.pow(diffVline, 2);
                  StdPred=StdPred+Math.pow(diffPredv, 2);
                
            }
            StdVline = Math.sqrt(StdVline);
            StdPred = Math.sqrt(StdPred);
            Cov=Cov/(this.View.lines.getRowCount()-1);
            double Corr= Cov/StdVline*StdPred;
             this.TargetSim=Corr;
        //  return utility;
    }
    
    public void ComputeTragetCorr(){
        
        DefaultTableModel PredAggData=GetPredAggData();
        //System.out.println("PredicateAggValue \t TargetAggValue");
        
        double [] vline= new double[this.View.lines.getRowCount()];
        double [] vPred= new double[this.View.lines.getRowCount()];
            for(int i=0;i<this.View.lines.getRowCount();i++){
                double VLine=Double.parseDouble( this.View.lines.getValueAt(i, 2).toString())/this.View.NormalizeV;
                vline[i]=VLine;
                double resPredcvalue=0;
                if (i<PredAggData.getRowCount()) resPredcvalue=Double.parseDouble( PredAggData.getValueAt(i, 1).toString());
                else  resPredcvalue=0;
                vPred[i]=resPredcvalue;
               
                
            }
//            Covariance Cov= new Covariance();
//            Variance Var= new Variance();
//            double s=Cov.covariance(vline, vPred);
//            double varLine=Var.evaluate(vline);
//            double varPred=Var.evaluate(vPred);
//            double cr= s/Math.sqrt(varLine)*Math.sqrt(varPred);
//              this.TargetSim=1-cr;
            PearsonsCorrelation p= new PearsonsCorrelation();
            
            double Corr= p.correlation(vline, vPred);
             this.TargetSim=1-Corr;
           
        //  return utility;
    }
  
    public void ComputeInflunce() {
      
          double utility = 0;
           double MaxInf = 0;
           double PredNoRec=this.NoRecods;
           
            for(int i=0;i<this.PredAggData.getRowCount();i++){
                String PreDResultIdName=this.PredAggData.getValueAt(i, 0).toString();
                double PredCurrV=Double.parseDouble(this.PredAggData.getValueAt(i, 1).toString());
                
                 for(int j=0;j<this.View.lines.getRowCount();j++){
                     String ResultIdName=this.View.lines.getValueAt(i, 0).toString();
                     double ActualV=Double.parseDouble(this.View.lines.getValueAt(j, 1).toString())/this.View.NormalizeV;
                
                if ( ResultIdName.equalsIgnoreCase(PreDResultIdName)){
                    utility =(ActualV-PredCurrV)/PredNoRec;
                    //
                    
               //vectorr
                if((PredCurrV/ActualV)>1) utility=utility*-1;
               else if((PredCurrV/ActualV)<1) utility=utility*1;
               else if((PredCurrV/ActualV)==1) utility=utility*0;
                
                if(utility>MaxInf) MaxInf= utility;
                }
                 }
            }
           // utility = Math.sqrt(utility);
             this.Influence=MaxInf;
         // return MaxInf;
      }
    
    public DefaultTableModel GetPredOrgData(){
        
        DefaultTableModel OrgData=new DefaultTableModel();
        try{
       // DefaultTableModel OrgData= new DefaultTableModel();
            String Stmt="";
            if(View.WhrCond.length()>0)
             Stmt=" select "+ View.AggAtt+"/"+View.NormalizeV+ " from "+ View.Table+" "+View.WhrCond+this.PredicateWehe;
            else
                Stmt=" select "+ View.AggAtt+"/"+View.NormalizeV+ " from "+ View.Table+" where "+this.PredicateWehe.substring(this.PredicateWehe.indexOf("and ")+4, this.PredicateWehe.length());
            
        ResultSet rs=M.ExecuteQryWithRS(Stmt);
         OrgData=(DefaultTableModel) DbUtils.resultSetToTableModel(rs);
         
         this.PredOrgData=OrgData;
         this.NoRecods=OrgData.getRowCount();
         this.ReductionFactor=this.NoRecods/View.OrgData.getRowCount();
       // return OrgData;
                
                }
        catch(Exception e){
                e.printStackTrace();
                }
        return OrgData;
    }
    
    public DefaultTableModel GetPredAggData(){
        
        DefaultTableModel PredAggData=new DefaultTableModel();
        
        try{
       // DefaultTableModel OrgData= new DefaultTableModel();
           if(this.PredAggData.getRowCount()<1){
               
            String Stmt="";
            if(View.WhrCond.length()>0)
                Stmt=" select "+View.GrbyAtt +","+View.AggFun+"("+ View.AggAtt+") from "+ View.Table+" "+View.WhrCond+" "+this.PredicateWehe+" group by "+View.GrbyAtt;
            else
                 Stmt=" select "+View.GrbyAtt +","+View.AggFun+"("+ View.AggAtt+") from "+ View.Table+" "+this.PredicateWehe.replaceFirst(" and ", " where ")+" group by "+View.GrbyAtt;
//                Stmt=" select "+View.GrbyAtt +","+View.AggFun+"("+ View.AggAtt+")  from "+ View.Table+" "+" where "+
//                        this.PredicateWehe.substring(this.PredicateWehe.indexOf("and ")+4, this.PredicateWehe.length())+ " group by "+View.GrbyAtt;
            
           // System.out.println(Stmt);
                ResultSet rs=M.ExecuteQryWithRS(Stmt);
                PredAggData=(DefaultTableModel) DbUtils.resultSetToTableModel(rs);
              // return OrgData;
        }
                this.PredAggData=PredAggData;
//                this.ValueAfterDel=Double.parseDouble(PredAggData.getValueAt(0, 1).toString());
                }
        catch(Exception e){
                e.printStackTrace();
                }
        return PredAggData;
    }
    
   
       
    public double GetPredNoRecords(){
          
           double rs=0;
      //  DefaultTableModel PredAggData=new DefaultTableModel();
        try{
        rs=M.getNumRowsWhre(this.PredicateWehe);
        this.NoRecods=rs;
                }
        catch(Exception e){
                e.printStackTrace();
                }
        return this.NoRecods;
    }
    
    public double GetPredAggMean(){
          
           // double rs=0;
      //  DefaultTableModel PredAggData=new DefaultTableModel();
        double Sum=0;
        for(int i=0;i<this.PredAggData.getRowCount();i++){
            double vaul=Double.parseDouble(this.PredAggData.getValueAt(i, 1).toString());
        Sum=Sum+vaul;
        }
        return Sum=Sum/this.PredAggData.getRowCount();
    }
    
    public void PredPrint(){
         System.out.println(this.PredName+"\t"+this.PredicateWehe+"\t"+this.NoRecods+"\t"+this.ValueAfterDel+"\t"+this.Influence+"\t"+this.InternalSim+"\t"+this.TargetSim);
    }
    
    public void PredAddinfo(DefaultTableModel PredMergedInfoModel ){
         
        Vector PredStatInf= new Vector();
        
                                        PredStatInf.add(this.PredName);
                                        PredStatInf.add(this.PredicateWehe);
//                                        PredStatInf.add(this.NoRecods);
//                                        PredStatInf.add(this.ValueAfterDel);
//                                        PredStatInf.add(this.Influence);
//                                        PredStatInf.add(this.InternalSim);
                                        PredStatInf.add(this.TargetSim);
        
                                        PredMergedInfoModel.addRow(PredStatInf);
       //  System.out.println(this.PredName+"\t"+this.PredicateWehe+"\t"+this.NoRecods+"\t"+this.ValueAfterDel+"\t"+this.Influence+"\t"+this.InternalSim+"\t"+this.TargetSim);
    }
    
    
    public XTR_FastPredicate FindPredicate(String PredName){
    
        XTR_FastPredicate Predicate=new XTR_FastPredicate();
        for(XTR_FastPredicate NaivePredicate : View.ViewPredicates){
            
            String CurPredName=NaivePredicate.PredicateWehe.toString();
            
            if(CurPredName.equalsIgnoreCase(PredName))
            {
                Predicate=NaivePredicate;
                return NaivePredicate;
            }
        }
            return Predicate;
    }
    
    // For Predicates Validation
    
     public void Store_PredAtts(String Attr_name,String Operator ,String PredAttValues, String F) {
        //flag C store in Categoral data 
        //     R store in Numeric data 
        DefaultTableModel PredData=new DefaultTableModel();
        if(PredAtts.getRowCount()<=0){
        PredAtts.addColumn("Attr_name");
        PredAtts.addColumn("Operator");
        PredAtts.addColumn("PredAttValues");
        PredAtts.addColumn("AttType");
        
        Vector PredAggvalues= new Vector();
                                        PredAggvalues.add(Attr_name);
                                        PredAggvalues.add(Operator);
                                        PredAggvalues.add(PredAttValues);
                                        PredAggvalues.add(F);
                                        
                                        this.PredAtts.addRow(PredAggvalues);
        }
        else{
            Vector PredAggvalues= new Vector();
                                        PredAggvalues.add(Attr_name);
                                        PredAggvalues.add(Operator);
                                        PredAggvalues.add(PredAttValues);
                                        PredAggvalues.add(F);
                                        
                                        this.PredAtts.addRow(PredAggvalues);
        }
                                       
    
    }
     
     public String Get_PredAtts(String Attr_name, XTR_FastPredicate P1) {
        //flag C store in Categoral data 
        //     R store in Numeric data 
        String AttCondition="";
//        if(PredAtts.getRowCount()<=0){
//        PredAtts.addColumn("Attr_name");
//        PredAtts.addColumn("Operator");
//        PredAtts.addColumn("PredAttValues");
//        PredAtts.addColumn("AttType");
        for(int i=0;i<P1.PredAtts.getRowCount();i++){
            String CurAtt=P1.PredAtts.getValueAt(i, 0).toString();
            String Operator=P1.PredAtts.getValueAt(i, 1).toString();
            String PredValues=P1.PredAtts.getValueAt(i, 2).toString();
            String AttType=P1.PredAtts.getValueAt(i, 3).toString();
            if(CurAtt.equalsIgnoreCase(Attr_name)){
                if(Operator.toUpperCase().equals("IN")|| Operator.toUpperCase().equals("NOT IN")){
                    AttCondition=Attr_name+" "+ Operator+" ("+PredValues+")";
                    this.Store_PredAtts(Attr_name,Operator,PredValues,AttType);
                    break;
                }
                else{
                    AttCondition=Attr_name+" "+ Operator+" "+PredValues;
                this.Store_PredAtts(Attr_name,Operator,PredValues,AttType);
                break;
                }
            }
        }
        
        return AttCondition;
                                       
    
    }
     
     public boolean IS_Exist(String Attr_name) {
        //flag C store in Categoral data 
        //     R store in Numeric data 
        boolean AttCondition=false;
//        if(PredAtts.getRowCount()<=0){
//        PredAtts.addColumn("Attr_name");
//        PredAtts.addColumn("Operator");
//        PredAtts.addColumn("PredAttValues");
//        PredAtts.addColumn("AttType");
        for(int i=0;i<PredAtts.getRowCount();i++){
            String CurAtt=PredAtts.getValueAt(i, 0).toString();
            
            if(CurAtt.equalsIgnoreCase(Attr_name)){
                    
                    AttCondition=true;
                    return true;
            }
              //  else
                //    return false;
            
        }
        
        return AttCondition;
                                       
    
    }
     
     public String NAIIVE_MergeP1_P2_Att(String MergedAtt,XTR_FastPredicate Predicate1,XTR_FastPredicate Predicate2){
         String P1_Att="";
         String P2_Att="";
         String P1_Op="";
         String P1_Predvalues=""; 
         String P1_PredAttType=""; 
         String P2_Op="";
         String P2_Predvalues=""; 
         String P2_PredAttType=""; 
                
            for (int i=0;i<Predicate1.PredAtts.getRowCount();i++){
             
                    
                     P1_Att=Predicate1.PredAtts.getValueAt(i, 0).toString();
                    if(P1_Att.equals(MergedAtt)|| P1_Att.contains(MergedAtt)){
                     P1_Op=Predicate1.PredAtts.getValueAt(i, 1).toString();
                     P1_Predvalues=Predicate1.PredAtts.getValueAt(i, 2).toString();
                     P1_PredAttType=Predicate1.PredAtts.getValueAt(i, 3).toString();
                        break;
                  
                    }
            }
                    
                    for (int j=0;j<Predicate2.PredAtts.getRowCount();j++){
             
                    
                     P2_Att=Predicate2.PredAtts.getValueAt(j, 0).toString();
                    if(P2_Att.equals(MergedAtt)|| P2_Att.contains(MergedAtt)){
                     P2_Op=Predicate2.PredAtts.getValueAt(j, 1).toString();
                     P2_Predvalues=Predicate2.PredAtts.getValueAt(j, 2).toString();
                     P2_PredAttType=Predicate2.PredAtts.getValueAt(j, 3).toString();
                        break;
                  
                    }
                    
                    }
                    // Start Modifying
                    
                    String MergedStmt="";
                      if(!OppistoeOP(P1_Op,P2_Op)) {
                    
                    if((P1_PredAttType.equals(P2_PredAttType) && P1_PredAttType.equals("C"))   ||
                            (P1_Att.equalsIgnoreCase(View.MaxSpit_AttName) && View.MaxSpit_AttType.toLowerCase().contains("char"))  )   //Merge Categorical Attr
                    {
                    if((P1_Op.equals("=")||P1_Op.toUpperCase().equals("IN")) && (P2_Op.equals("=")||P2_Op.toUpperCase().equals("IN"))){
                        String Predvalues=GetUnRedundantCatPredValues(P1_Op,P1_Predvalues,P2_Op,P2_Predvalues);
                        MergedStmt=" and "+MergedAtt+" IN("+ Predvalues+")";
                        Store_PredAtts( MergedAtt, "IN" , Predvalues,  "C");
                        
                    }
                    else if((P1_Op.equals("<>")||P1_Op.toUpperCase().equals("NOT IN")) && (P2_Op.equals("<>")||P2_Op.toUpperCase().equals("NOT IN"))){
                        String Predvalues=GetUnRedundantCatPredValues(P1_Op,P1_Predvalues,P2_Op,P2_Predvalues);
                        MergedStmt=" and "+MergedAtt+" NOT IN("+ Predvalues+")";
                        
                        Store_PredAtts( MergedAtt, "NOT IN" , Predvalues,  "C");
                    }
                    
                     }
                    else if(P1_PredAttType.equals(P2_PredAttType) && P1_PredAttType.equals("R") )
                           // && (!P1_Att.toLowerCase().contains("id")||!P1_Att.toLowerCase().contains("ids"))) //Merge Numeric Attr
                    {
                       
                         if((P1_Op.equals("=")||P1_Op.toUpperCase().equals("BETWEEN")) && (P2_Op.equals("=")||P2_Op.toUpperCase().equals("BETWEEN" ))){
                             String Predvalues=GetUnRedundantNumPredValues(P1_Op,P1_Predvalues,P2_Op,P2_Predvalues);       
                             MergedStmt=" and "+MergedAtt+" BETWEEN" + Predvalues;
                              Store_PredAtts( MergedAtt, "BETWEEN" , Predvalues,  "R");
                         }
                            else if((P1_Op.equals("<>")||P1_Op.toUpperCase().equals("NOT BETWEEN")) && (P2_Op.equals("<>")||P2_Op.toUpperCase().equals("NOT BETWEEN"))){
                               String Predvalues=GetUnRedundantNumPredValues(P1_Op,P1_Predvalues,P2_Op,P2_Predvalues);       
                               // MergedStmt=" and "+MergedAtt+ " NOT Between "+ GetUnRedundantNumPredValues(P1_Op,P1_Predvalues,P2_Op,P2_Predvalues);
                                MergedStmt=" and "+MergedAtt+" NOT BETWEEN "+ Predvalues;
                                Store_PredAtts( MergedAtt, "NOT BETWEEN" , Predvalues,  "R");
                            }
                    }
            
     }
                      else
                          MergedStmt=Predicate1.PredicateWehe+" and "+Predicate2.PredicateWehe;
                      
            return MergedStmt;
            }
     
     
     
     
     public String XTR_Basic_MergeP1_P2_Att (String MergedAtt,XTR_FastPredicate Predicate1,XTR_FastPredicate Predicate2){
         String P1_Att="";
         String P2_Att="";
         String P1_Op="";
         String P1_Predvalues=""; 
         String P1_PredAttType=""; 
         String P2_Op="";
         String P2_Predvalues=""; 
         String P2_PredAttType=""; 
                
            for (int i=0;i<Predicate1.PredAtts.getRowCount();i++){
             
                    
                     P1_Att=Predicate1.PredAtts.getValueAt(i, 0).toString();
                    if(P1_Att.equals(MergedAtt)|| P1_Att.equals(MergedAtt)){
                     P1_Op=Predicate1.PredAtts.getValueAt(i, 1).toString();
                     P1_Predvalues=Predicate1.PredAtts.getValueAt(i, 2).toString();
                     P1_PredAttType=Predicate1.PredAtts.getValueAt(i, 3).toString();
                        break;
                  
                    }
            }
                    
                    for (int j=0;j<Predicate2.PredAtts.getRowCount();j++){
             
                    
                     P2_Att=Predicate2.PredAtts.getValueAt(j, 0).toString();
                    if(P2_Att.equals(MergedAtt)|| P2_Att.equals(MergedAtt)){
                     P2_Op=Predicate2.PredAtts.getValueAt(j, 1).toString();
                     P2_Predvalues=Predicate2.PredAtts.getValueAt(j, 2).toString();
                     P2_PredAttType=Predicate2.PredAtts.getValueAt(j, 3).toString();
                        break;
                  
                    }
                    
                    }
                    // Start Modifying
                    
                    String MergedStmt="";
                      if(!OppistoeOP(P1_Op,P2_Op)) {
                    
                    if((P1_PredAttType.equals(P2_PredAttType) && P1_PredAttType.equals("C"))   ||
                            (MergedAtt.equalsIgnoreCase(View.MaxSpit_AttName) )   )   //Merge Categorical Attr
                    {
                    if((P1_Op.equals("=")||P1_Op.toUpperCase().equals("IN")) && (P2_Op.equals("=")||P2_Op.toUpperCase().equals("IN"))){
                        
                       // if(P1_Att.equalsIgnoreCase(View.MaxSpit_AttName)&&  View.MaxSpit_AttType.toLowerCase().contains("char")){
                        String Predvalues=GetUnRedundantCatPredValues(P1_Op,P1_Predvalues,P2_Op,P2_Predvalues);
                        MergedStmt=" and "+MergedAtt+" IN("+ Predvalues+")";
                        Store_PredAtts( MergedAtt, "IN" , Predvalues,  "C");
//                        }
//                        else
//                        {
//                        
//                        }
                        
                        
                    }
                    else if((P1_Op.equals("<>")||P1_Op.toUpperCase().equals("NOT IN")) && (P2_Op.equals("<>")||P2_Op.toUpperCase().equals("NOT IN"))){
                        String Predvalues=GetUnRedundantCatPredValues(P1_Op,P1_Predvalues,P2_Op,P2_Predvalues);
                        MergedStmt=" and "+MergedAtt+" NOT IN("+ Predvalues+")";
                        
                        Store_PredAtts( MergedAtt, "NOT IN" , Predvalues,  "C");
                    }
                    
                     }
                    else if(P1_PredAttType.equals(P2_PredAttType) && P1_PredAttType.equals("R") &&  (!MergedAtt.equalsIgnoreCase(View.MaxSpit_AttName) )  )
                           // && (!P1_Att.toLowerCase().contains("id")||!P1_Att.toLowerCase().contains("ids"))) //Merge Numeric Attr
                    {
                       
                         if((P1_Op.equals("=")||P1_Op.toUpperCase().equals("BETWEEN")) && (P2_Op.equals("=")||P2_Op.toUpperCase().equals("BETWEEN" ))){
                             String Predvalues=GetUnRedundantNumPredValues(P1_Op,P1_Predvalues,P2_Op,P2_Predvalues);       
                             MergedStmt=" and "+MergedAtt+" BETWEEN" + Predvalues;
                              Store_PredAtts( MergedAtt, "BETWEEN" , Predvalues,  "R");
                         }
                            else if((P1_Op.equals("<>")||P1_Op.toUpperCase().equals("NOT BETWEEN")) && (P2_Op.equals("<>")||P2_Op.toUpperCase().equals("NOT BETWEEN"))){
                               String Predvalues=GetUnRedundantNumPredValues(P1_Op,P1_Predvalues,P2_Op,P2_Predvalues);       
                               // MergedStmt=" and "+MergedAtt+ " NOT Between "+ GetUnRedundantNumPredValues(P1_Op,P1_Predvalues,P2_Op,P2_Predvalues);
                                MergedStmt=" and "+MergedAtt+" NOT BETWEEN "+ Predvalues;
                                Store_PredAtts( MergedAtt, "NOT BETWEEN" , Predvalues,  "R");
                            }
                    }
            
     }
                      else
                          MergedStmt=Predicate1.PredicateWehe+" "+Predicate2.PredicateWehe;
                      
            return MergedStmt;
            }
     
     public String GetUnRedundantCatPredValues(String PredicateOp1,String PredicateV1,String PredicateOp2,String PredicateV2){
            String MregValue="";
            
        if(PredicateOp1.equals("=")||PredicateOp1.equals("<>")){
                //if Not exist then Add
            if(!PredicateV2.contains(PredicateV1)) 
                return MregValue=PredicateV1+","+PredicateV2;
            else
                return MregValue=PredicateV2;
            
        }
        
        if(PredicateOp2.equals("=")||PredicateOp2.equals("<>")){
                //if Not exist then Add
            if(!PredicateV1.contains(PredicateV2)) 
               return MregValue=PredicateV1+","+PredicateV2;
            else
                return MregValue=PredicateV1;
        }
        else if(PredicateOp2.toLowerCase().contains("in")||PredicateOp1.toLowerCase().contains("in"))
        {   String St_short=PredicateV1;
            String St_Long=PredicateV2;
            if(PredicateV1.length()<PredicateV2.length()) {  
             St_short=PredicateV1;
             St_Long=PredicateV2;
            }
            else
            {
                St_short=PredicateV2;
                St_Long=PredicateV1;
            }
            
            // Find Short values in Longer Ones
            while(St_short.length()>=1){
                String Singleval="";
                if(St_short.contains(","))
                    if(St_short.startsWith(","))
                        St_short=St_short.substring(1, St_short.length());
                    //Singleval=St_short.substring(1, St_short.indexOf(","));
                      //  Singleval=St_short.substring(1, St_short.length());
                    else
                    Singleval=St_short.substring(0, St_short.indexOf(","));
                
                else
                    Singleval=St_short.substring(0, St_short.length());
                
                if(!St_Long.contains(Singleval))
                    St_Long=St_Long+","+Singleval;
                
                St_short=St_short.substring(St_short.indexOf(Singleval)+Singleval.length(), St_short.length());
            }
            MregValue=St_Long;
        }
        return MregValue;
    }
     
     public String GetUnRedundantNumPredValues(String PredicateOp1,String PredicateV1,String PredicateOp2,String PredicateV2){
            String MregValue="";
            double LOW=0;
            double HIGH=0;
           // PredicateOp1=PredicateOp1.replaceAll(" ", "");
            //PredicateOp2=PredicateOp2.replaceAll(" ", "");
      
            
        if((PredicateOp1.equals("=")||PredicateOp1.equals("<>")) &&
                        (PredicateOp2.equals("=")||PredicateOp2.equals("<>")) ){
                //
            double V1=Double.parseDouble(PredicateV1);
            double V2=Double.parseDouble(PredicateV2);
            LOW= Math.min(V2, V1);
            HIGH= Math.max(V2, V1);
                return MregValue=LOW+" AND "+HIGH;
            
        }
        
        else if((PredicateOp1.equals("=")||PredicateOp1.equals("<>")) &&
                       ( PredicateOp2.toUpperCase().equals("BETWEEN")||PredicateOp2.toUpperCase().equals("NOT BETWEEN") ) ){
                //
            double V1=Double.parseDouble(PredicateV1);
            double V2_Low=Double.parseDouble(PredicateV2.toLowerCase().substring(0, PredicateV2.indexOf(" AND ")));
           // double V2_High=Double.parseDouble(PredicateV2.substring(PredicateV2.indexOf(" and ")+4,PredicateV2.length()));
            double V2_High=Double.parseDouble(PredicateV2.toLowerCase().substring(PredicateV2.indexOf(" AND ")+4));
           // System.out.println(V2_Low+">>>"+V2_High);
            LOW= Math.min(V2_Low, V1);
            HIGH= Math.max(V2_High, V1);
                return MregValue=LOW+" AND "+HIGH;
            
        }
        else if((PredicateOp2.equals("=")||PredicateOp2.equals("<>")) &&
                        (PredicateOp1.toUpperCase().equals("BETWEEN")||PredicateOp1.toUpperCase().equals("NOT BETWEEN")) ){
                //
            double V2=Double.parseDouble(PredicateV2);
            double V1_Low=Double.parseDouble(PredicateV1.toLowerCase().substring(0, PredicateV1.indexOf(" AND ")));
            //double V1_High=Double.parseDouble(PredicateV1.substring(PredicateV1.indexOf(" and ")+4,PredicateV1.length()));
            double V1_High=Double.parseDouble(PredicateV1.toLowerCase().substring(PredicateV1.indexOf(" AND ")+4));
           // System.out.println(V1_Low+">>>"+V1_High);
            LOW= Math.min(V1_Low, V2);
            HIGH= Math.max(V1_High, V2);
                return MregValue=LOW+" AND "+HIGH;
            
        }
        
        else if( (PredicateOp1.toUpperCase().equals("BETWEEN")||PredicateOp1.toUpperCase().equals("NOT BETWEEN") ) &&
                        (PredicateOp2.equals("BETWEEN")||PredicateOp2.equals("NOT BETWEEN") ) ){
                //
           // System.out.println(" Entering.............==========");
            double V1_Low=Double.parseDouble(PredicateV1.toLowerCase().substring(0, PredicateV1.indexOf(" AND ")));
            //double V1_High=Double.parseDouble(PredicateV1.substring(PredicateV1.indexOf(" and ")+4,PredicateV1.length()));
            double V1_High=Double.parseDouble(PredicateV1.toLowerCase().substring(PredicateV1.indexOf(" AND ")+4));
            //System.out.println(V1_Low+">>>"+V1_High);
            double V2_Low=Double.parseDouble(PredicateV2.toLowerCase().substring(0, PredicateV2.indexOf(" AND ")));
            //double V2_High=Double.parseDouble(PredicateV2.substring(PredicateV2.indexOf(" and ")+4,PredicateV2.length()));
            double V2_High=Double.parseDouble(PredicateV2.toLowerCase().substring(PredicateV2.indexOf(" AND ")+4));
            LOW= Math.min(V1_Low, V2_Low);
            HIGH= Math.max(V1_High, V2_High);
                return MregValue=LOW+" AND "+HIGH;
            
        }
        
     
        
        return MregValue;
    }
     
     public String ModifyMergedPedicates(XTR_FastPredicate P1 ,XTR_FastPredicate P2 ){
        
       // String ExeculdedAttr=Predicate1+" " +Predicate2+this.CurView.AggAtt+" " +this.CurView.WhrCond;
        String ExeculdedAttr=this.View.GrbyAtt+this.View.AggAtt+" " +this.View.WhrCond;
        String ModifiedPred=" ";
//        InMemPredicate P1=this.View.GetFastPred(Predicate1);
//        InMemPredicate P2=this.View.GetFastPred(Predicate2);
        try{
            Map<String, String> columnAttributes = this.M.getColumnAttributes();
//            Predicate1=ReversePredicate( Predicate1);
//            Predicate2=ReversePredicate( Predicate2);
            for (Map.Entry<String, String> columnAttribute1 : columnAttributes.entrySet()){
		
			String attr = columnAttribute1.getKey().toString().replaceAll(" ", "");
                        if (!ExeculdedAttr.contains(attr)) {
                            
                         if (P1.IS_Exist(attr)&& P2.IS_Exist(attr))
                             
                             //ModifiedPred=ModifiedPred+NAIIVE_MergeP1_P2_Att( attr, P1, P2);
                         ModifiedPred=ModifiedPred+XTR_Basic_MergeP1_P2_Att( attr, P1, P2);
                         
                         
                         //else if (P1.PredicateWehe.toLowerCase().equals(attr)){
                         else if (P1.IS_Exist(attr)){    
                                   // if(P1.PredicateWehe.toLowerCase().contains(" where ")) 
                                        //ModifiedPred=ModifiedPred+P1.PredicateWehe.replaceFirst(" where ", " and ");
                                        ModifiedPred=ModifiedPred+" and "+this.Get_PredAtts( attr,P1);
                                   // else ModifiedPred=ModifiedPred+P1.PredicateWehe;
                         }
                         //else if (P2.PredicateWehe.toLowerCase().equals(attr)){
                         else if (P2.IS_Exist(attr)){    
                                    //if(P2.PredicateWehe.toLowerCase().contains(" where ")) ModifiedPred=ModifiedPred+P2.PredicateWehe.replaceFirst(" where ", " and ");
                                    //else ModifiedPred=ModifiedPred+P2.PredicateWehe;
                                     ModifiedPred=ModifiedPred+" and "+ this.Get_PredAtts( attr,P2);
                         }
                         
                        }
                       
            } 
                     
      //   ModifiedPred=ModifiedPred.replaceFirst(" and ", " where ");
                        return ModifiedPred;
        } catch(Exception e){
        e.printStackTrace();
        }
         return ModifiedPred;
    }
     
     public boolean OppistoeOP(String PredicateOp1,String PredicateOp2){
     
          boolean  OppistoeO=true;
          
            if((PredicateOp1.equals("=") && PredicateOp2.toUpperCase().equals("<>")) ||  
                    (PredicateOp2.equals("=") && PredicateOp1.toUpperCase().equals("<>") )   )   
                    OppistoeO=true;
            else if((PredicateOp1.equals("=") && PredicateOp2.toUpperCase().equals("NOT IN")) ||  
                    (PredicateOp2.equals("=") && PredicateOp1.toUpperCase().equals("NOT IN") )   )   
                    OppistoeO=true;
            else if((PredicateOp1.equals("<>") && PredicateOp2.toUpperCase().equals("IN")) ||  
                    (PredicateOp2.equals("<>") && PredicateOp1.toUpperCase().equals("IN") )   )   
                    OppistoeO=true;
            else if((PredicateOp1.equals("IN") && PredicateOp2.toUpperCase().equals("NOT IN")) ||  
                    (PredicateOp2.equals("IN") && PredicateOp1.toUpperCase().equals("NOT IN") )   )   
                    OppistoeO=true;
            else if((PredicateOp1.equals("=") && PredicateOp2.toUpperCase().equals("NOT BETWEEN")) ||  
                    (PredicateOp2.equals("=") && PredicateOp1.toUpperCase().equals("NOT BETWEEN") )   )   
                    OppistoeO=true;
            else if((PredicateOp1.equals("<>") && PredicateOp2.toUpperCase().equals("BETWEEN")) ||  
                    (PredicateOp2.equals("<>") && PredicateOp1.toUpperCase().equals("BETWEEN") )   )   
                    OppistoeO=true;
            else if((PredicateOp1.equals("BETWEEN") && PredicateOp2.toUpperCase().equals("NOT BETWEEN")) ||  
                    (PredicateOp2.equals("BETWEEN") && PredicateOp1.toUpperCase().equals("NOT BETWEEN") )   )   
                    OppistoeO=true;
            else
                      OppistoeO=false;
            
            return OppistoeO;
    }
     
     
}
