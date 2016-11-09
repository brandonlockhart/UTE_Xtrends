/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package explelan;

import explelan.View.Metric;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import metadata.Metadata;

/**
 *
 * @author uqiibra2
 */
public class Spliiter  {
    public View V;
    public Metadata MD;
    String MaxSplitAtt="";
    String MaxSplitAttType="";
    
    public void getMaxSplit(){
    
        String CurrentAtts=V.AggAtt+V.GrbyAtt;
        String MatAtt="";
       // String MatAttType="";
        double MAxDistinct=0;
        double  AttDistinctCount=0;
        try{
            Map<String, String> RemainCols = MD.getColumnAttributes();
         //   String type = rs.getString("TYPE_NAME");
                        
               for (Map.Entry<String, String> columnAttribute : RemainCols.entrySet()){
		
			String name = columnAttribute.getKey().toString();
                        String Type = columnAttribute.getValue();
                        if (!CurrentAtts.contains(name)){
                            if(V.WhrCond.length()>0)
                                AttDistinctCount=MD.getNumDistinctWhr(name," where "+ V.WhrCond);
                            else
                                AttDistinctCount=MD.getNumDistinctWhr(name, V.WhrCond);
                              //
                              if(AttDistinctCount>MAxDistinct) {
                                  MatAtt=name; 
                                  MAxDistinct=AttDistinctCount;
                                 if (Type.toLowerCase().startsWith("varchar"))
                                this.MaxSplitAttType="C";
                                else
                                this.MaxSplitAttType="R";
                              }
                        }
			
                        //
                       
                        
		}
             System.out.println("Max Spllitting Att:"+MatAtt);
             this.MaxSplitAtt=MatAtt;
             
        }
        catch (Exception e){
            e.printStackTrace();
        }
        
    }
    
   
    
    public DefaultTableModel StartWithResID(String Attr){
        
         DefaultTableModel AllPredicates=new DefaultTableModel();
         AllPredicates.addColumn("PredName");
         AllPredicates.addColumn("PredicateWehe");
         AllPredicates.addColumn("NoRecods");
         AllPredicates.addColumn("ValueafterDel");
         AllPredicates.addColumn("Influence");
         AllPredicates.addColumn("InternalSim");
         AllPredicates.addColumn("TargetSim");
        
        try{
                String AttrType=this.MaxSplitAttType;
                String Predicate="";
                String PredIds="";
                String GlobalViewWher=V.WhrCond;
               
                V.VMD=this.MD;
               
                // Search By Result id 
                for(int i=0;i<V.lines.getRowCount();i++){
                    String ResultId=V.lines.getValueAt(i, 0).toString();
                    if(V.WhrCond.length()==0)
                        V.WhrCond= " where "+ V.GrbyAtt +" ='"+ResultId+"'";
                    else
                         V.WhrCond= V.WhrCond+ " and "+ V.GrbyAtt +" ='"+ResultId+"'";
                         V.OrgData=V.GetOrgData();
                         
                    System.out.println("Expolring "+ ResultId +" Data");
                    
                      PredIds="Select distinct "+Attr+" from "+V.Table+ V.WhrCond;
                      ResultSet rs=MD.ExecuteQryWithRS(PredIds);
                      System.out.println("PredName\t PredicateWehe \t NoRecods \t ValueafterDel \t Influence \t InternalSim\t TargetSim");
                      
                                      // Slect All Distinct Values in This Attribute
               
                                    while(rs.next()){
                                        String ids=rs.getString(1);
                                        if (AttrType.equalsIgnoreCase("C"))
                                        Predicate=V.WhrCond+" and "+Attr+ "<>'"+ids+"'";

                                        else if (AttrType.equalsIgnoreCase("R"))
                                        Predicate=V.WhrCond+" and "+Attr+ "<>"+ids;

                                        Predicate P= new Predicate();
                                         P.View=V;
                                         P.M=this.MD;
                                         P.PredName=Attr+ "="+ids;
                                         P.PredicateWehe= Predicate;
                                      // Evaulate Predicate
                                          P.ComputeInternalSimilirity();
                                          P.ComputeTragetSimilirity();
                                          P.ComputeInflunce();
                                          P.GetPredNoRecords();
                                        // Storing......
                                        Vector PredStatInf= new Vector();
                                        PredStatInf.add(P.PredName);
                                        PredStatInf.add(P.PredicateWehe);
                                        PredStatInf.add(P.NoRecods);
                                        PredStatInf.add(P.ValueAfterDel);
                                        PredStatInf.add(P.Influence);
                                        PredStatInf.add(P.InternalSim);
                                        PredStatInf.add(P.TargetSim);
                                        AllPredicates.addRow(PredStatInf);

                                        System.out.println(P.PredName+"\t"+P.PredicateWehe+"\t"+P.NoRecods+"\t"+P.ValueAfterDel+"\t"+P.Influence+"\t"+P.InternalSim+"\t"+P.TargetSim);


                                    }
                                    V.WhrCond=GlobalViewWher;
                }
                
                  
                
        
        }   catch (Exception e){
            e.printStackTrace();
        }
        
        return AllPredicates;
    } 
    
    public DefaultTableModel StartWith(String Attr){
        
         DefaultTableModel AllPredicates=new DefaultTableModel();
         AllPredicates.addColumn("PredName");
         AllPredicates.addColumn("PredicateWehe");
         AllPredicates.addColumn("NoRecods");
         AllPredicates.addColumn("ValueafterDel");
         AllPredicates.addColumn("Influence");
         AllPredicates.addColumn("PU2-DistanceCorr");
         AllPredicates.addColumn("PU1-L2 norm");
        
        try{
                String AttrType=this.MaxSplitAttType;
                String Predicate="";
                String PredIds="";
              
                    if(V.WhrCond.length()>0)
                           V.WhrCond= " where "+ V.WhrCond;
                  
                         
                    System.out.println("Expolring Data by attr: "+ Attr +" ");
                    
                      PredIds="Select distinct "+Attr+" from "+V.Table+ V.WhrCond;
                   //   System.out.println(PredIds);
                      ResultSet rs=MD.ExecuteQryWithRS(PredIds);
                      //System.out.println("|PredName|\t |PredicateWehe| \t |NoRecods| \t |ValueafterDel| \t |Influence \t |PU-Distance|\t TargetSim");
                      System.out.println("|PredName|\t |PredicateWehe| \t |NoRecods| \t |PU-Distance|");
                      
                                      // Slect All Distinct Values in This Attribute
               
                                    while(rs.next()){
                                        String ids=rs.getString(1);
                                        if (AttrType.equalsIgnoreCase("C"))
                                        Predicate=V.WhrCond+" and "+Attr+ "<>'"+ids+"'";

                                        else if (AttrType.equalsIgnoreCase("R"))
                                        Predicate=V.WhrCond+" and "+Attr+ "<>"+ids;

                                        Predicate P= new Predicate();
                                         P.View=V;
                                         P.M=this.MD;
                                         P.PredName=Attr+ "="+ids;
                                         P.PredicateWehe= Predicate;
                                         // Evaulate Predicate
                                         if (V.CurrMetic==Metric.PU_l2_norm){
                                             P.ComputeTragetSimilirity();
                                             System.out.println(P.PredName+"\t"+P.PredicateWehe+"\t"+P.NoRecods+"\t"+"\t"+P.TargetSim);
                                         }
                                         else if (V.CurrMetic==Metric.PU_Corr_distnace){
                                             P.ComputeTragetCorr();
                                             //P.ComputeTragetPCorrDis();
                                             System.out.println(P.PredName+"\t"+P.PredicateWehe+"\t"+P.NoRecods+"\t"+"\t"+(P.TargetSim));
                                         }
                                         else if (V.CurrMetic==Metric.PI_Influence){
                                             P.ComputeInflunce();
                                             System.out.println(P.PredName+"\t"+P.PredicateWehe+"\t"+P.NoRecods+"\t"+"\t"+P.Influence);
                                         }
                                             //P.ComputeInternalSimilirity();
                                      
                                          
                                          
                                          //
                                          //P.GetPredNoRecords();
                                        // Storing......
                                        Vector PredStatInf= new Vector();
                                        PredStatInf.add(P.PredName);
                                        PredStatInf.add(P.PredicateWehe);
                                        PredStatInf.add(P.NoRecods);
                                        PredStatInf.add(P.ValueAfterDel);
                                        PredStatInf.add(P.Influence);
                                        PredStatInf.add(P.InternalSim);
                                        PredStatInf.add(P.TargetSim);
                                        AllPredicates.addRow(PredStatInf);

                                        //System.out.println(P.PredName+"\t"+P.PredicateWehe+"\t"+P.NoRecods+"\t"+"\t"+P.TargetSim);


                                    }
                                //    V.WhrCond=GlobalViewWher;
               // }
                
                  
                
        
        }   catch (Exception e){
            e.printStackTrace();
        }
        
        return AllPredicates;
    } 
    
     public DefaultTableModel StartWithThrshld(String Attr, double SimlirtyDegree,double pvalue){
        
         DefaultTableModel AllPredicates=new DefaultTableModel();
         AllPredicates.addColumn("PredName");
         AllPredicates.addColumn("PredicateWehe");
         AllPredicates.addColumn("NoRecods");
         AllPredicates.addColumn("ValueafterDel");
         AllPredicates.addColumn("Influence");
         AllPredicates.addColumn("PU2-DistanceCorr");
         AllPredicates.addColumn("PU1-L2 norm");
        
         double miNThr=0;
         double maXThr=0;
         //double SimlirtyDegree=0.01; //Typical Similiarity
        // double pvalue=0.9;
         
         
        try{
                String AttrType=this.MaxSplitAttType;
                String Predicate="";
                String PredIds="";
              
                    if(V.WhrCond.length()>0)
                           V.WhrCond= " where "+ V.WhrCond;
                  
                         
                    System.out.println("Expolring Data by attr: "+ Attr +" ");
                    
                      PredIds="Select distinct "+Attr+" from "+V.Table+ V.WhrCond;
                      ResultSet rs=MD.ExecuteQryWithRS(PredIds);
                      //System.out.println("|PredName|\t |PredicateWehe| \t |NoRecods| \t |ValueafterDel| \t |Influence \t |PU-Distance|\t TargetSim");
                      System.out.println("|PredName|\t |PredicateWehe| \t |NoRecods| \t |PU-Distance|");
                      
                                      // Slect All Distinct Values in This Attribute
               double PredThrd=0;
               int i=1;
                                    while(rs.next()){
                                        String ids=rs.getString(1);
                                        if (AttrType.equalsIgnoreCase("C"))
                                        Predicate=V.WhrCond+" and "+Attr+ "<>'"+ids+"'";

                                        else if (AttrType.equalsIgnoreCase("R"))
                                        Predicate=V.WhrCond+" and "+Attr+ "<>"+ids;
                                        
                                      
                                        Predicate P= new Predicate();
                                         P.View=V;
                                         P.M=this.MD;
                                         P.PredName=Attr+ "="+ids;
                                         P.PredicateWehe= Predicate;
                                         // Evaulate Predicate
                                         if (V.CurrMetic==Metric.PU_l2_norm){
                                             P.ComputeTragetSimilirity();
                                             System.out.println(P.PredName+"\t"+P.PredicateWehe+"\t"+P.NoRecods+"\t"+"\t"+P.TargetSim);
                                         }
                                         else if (V.CurrMetic==Metric.PU_Corr_distnace){
                                             P.ComputeTragetCorr();
                                             //P.ComputeTragetPCorrDis();
                                             System.out.println(P.PredName+"\t"+P.PredicateWehe+"\t"+P.NoRecods+"\t"+"\t"+(P.TargetSim));
                                         }
                                         else if (V.CurrMetic==Metric.PI_Influence){
                                             P.ComputeInflunce();
                                             System.out.println(P.PredName+"\t"+P.PredicateWehe+"\t"+P.NoRecods+"\t"+"\t"+P.Influence);
                                         }
                                             //P.ComputeInternalSimilirity();
                                      
                                          
                                          
                                          //
                                          //P.GetPredNoRecords();
                                        // Storing......
                                        Vector PredStatInf= new Vector();
                                        PredStatInf.add(P.PredName);
                                        PredStatInf.add(P.PredicateWehe);
                                        PredStatInf.add(P.NoRecods);
                                        PredStatInf.add(P.ValueAfterDel);
                                        PredStatInf.add(P.Influence);
                                        PredStatInf.add(P.InternalSim);
                                        PredStatInf.add(P.TargetSim);
                                        AllPredicates.addRow(PredStatInf);
                                            
                                        if (i==1){
                                            maXThr=1-P.TargetSim;
                                            miNThr=1-P.TargetSim;
                                          //  PredThrd=ComputThr( pvalue,  miNThr,  maXThr,  SimlirtyDegree); 
                                            i++;
                                        }
                                        else
                                             PredThrd=ComputThr( pvalue,  miNThr,  maXThr,  SimlirtyDegree); 
                                        //System.out.println(P.PredName+"\t"+P.PredicateWehe+"\t"+P.NoRecods+"\t"+"\t"+P.TargetSim);
                                                if(PredThrd<(P.TargetSim)){
                                                    maXThr=Math.max(maXThr, (P.TargetSim));
                                                    miNThr=Math.min(miNThr, (P.TargetSim));
                                                }
                                                else {
                                                System.out.println("Over Threshold Exit......");
                                               break;
                                                }

                                    }
                                //    V.WhrCond=GlobalViewWher;
               // }
                
                  
                
        
        }   catch (Exception e){
            e.printStackTrace();
        }
        
        return AllPredicates;
    } 
    
     public double ComputSlop(double pvalue, double minThr, double maXThr){
     
         
         double slope=0;
         slope=(minThr-maXThr);
         double v1=(1-pvalue)*0; //Upper Inf
         double v2=pvalue*this.V.LowerSimlirity; //Lower inf
         
         slope=slope/(v1-v2);
         return slope;
     }
     
     public double ComputThr(double pvalue, double minThr, double maXThr, double SimilrtDegree){
     
         
         double slope=ComputSlop(pvalue,minThr,maXThr);
         double w=Math.min(minThr+(slope*(0-SimilrtDegree)), maXThr);
         
         double Thrshld=w*(0-this.V.LowerSimlirity); //Max Inf
         System.out.println(Thrshld);
         return Thrshld;
     }
    
}
