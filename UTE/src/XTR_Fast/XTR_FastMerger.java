/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package XTR_Fast;

//import naiive.*;
//import explelan.*;
//import XTR_Basic.*;
//import InMem.InMemView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import metadata.ColumnSorter;
import metadata.Metadata;

/**
 *
 * @author uqiibra2
 */
public class XTR_FastMerger {
    
    public Metadata Md;
    public XTR_FastView CurView;
    //DefaultTableModel PredMergedInfo=new DefaultTableModel ();
    
    double MergedSimilirty=0;
    
    
   public void Start_Basic() {
    
           CurView.NearestTargetSim= Double.parseDouble( CurView.AllSortedPredicates.getValueAt(0, 3).toString());
           CurView.TopPredicate=CurView.AllSortedPredicates.getValueAt(0, 1).toString();
           
           
        this.MergedSimilirty=CurView.NearestTargetSim;
        int i=1;
                while(this.MergedSimilirty<=CurView.NearestTargetSim && i<CurView.AllSortedPredicates.getRowCount()){
                
                    // Merge
                    XTR_FastPredicate MergedPredicate= new XTR_FastPredicate();
                    MergedPredicate.View=CurView;
                    MergedPredicate.M=CurView.VMD;
                    
                    String PredicateWHER=CurView.AllSortedPredicates.getValueAt(i, 1).toString();
                    XTR_FastPredicate TOPPredicate= FindFastPredicate(CurView.TopPredicate);
                    XTR_FastPredicate Predicate= FindFastPredicate(PredicateWHER);
                    
                   // Merge only Predicates have Target Similirity 
                    if(!Double.isNaN(Predicate.TargetSim))
                    {     
                    //Get right Condition i.e. remove duplications 
                    
                    MergedPredicate.PredicateWehe= MergedPredicate.ModifyMergedPedicates(TOPPredicate, Predicate);
                    MergedPredicate.PredName="External Merged Predicate";
                    
                    if( !MergedPredicate.PredicateWehe.replaceAll(" ", "").equalsIgnoreCase(TOPPredicate.PredicateWehe.replaceAll(" ", ""))) {
                    MergedPredicate.ComputeTragetSimilirity();
                    this.MergedSimilirty=MergedPredicate.TargetSim;
                    MergedPredicate.InternalSim=Double.NaN;
                    
                    System.out.println(MergedPredicate.PredName+"|\t"+MergedPredicate.PredicateWehe+"|\t"+MergedPredicate.TargetSim); 
                    
                             if(MergedPredicate.TargetSim<=CurView.NearestTargetSim)
                                    {
                                        CurView.TopPredicate=MergedPredicate.PredicateWehe;
                                        CurView.NearestTargetSim=MergedPredicate.TargetSim;

                                      

                                    }
                             
                               // Store New Merged Predicate;
                                        CurView.ViewPredicates.add(MergedPredicate);
                                        Vector PredStatInf= new Vector();
                                                        PredStatInf.add(MergedPredicate.PredName);
                                                        PredStatInf.add(MergedPredicate.PredicateWehe);
                //                                        PredStatInf.add(P.NoRecods);
                //                                        PredStatInf.add(P.ValueAfterDel);
                //                                        PredStatInf.add(P.Influence);
                                                        PredStatInf.add(MergedPredicate.InternalSim);
                                                        PredStatInf.add(MergedPredicate.TargetSim);
                                                        
                                        CurView.AllSortedPredicates.addRow(PredStatInf);
                                      //  BestMergedCAtt_S1(MergedPredicate);
                                       // BestMergedRAtt(MergedPredicate);
                                        
                    }
                }
//                    else
//                        System.out.println("ALready Merged Before !!!!!!!");
                    i++;
                    
                    
                }
                    
              
                    
                    //Sort Neasest distance to target 
                                    Vector AllPredicates_data = CurView.AllSortedPredicates.getDataVector();
                                    Collections.sort(AllPredicates_data, new ColumnSorter(3, true));
    }
   
    public void Start_Advanced() {
    
           CurView.NearestTargetSim= Double.parseDouble( CurView.AllSortedPredicates.getValueAt(0, 3).toString());
           CurView.TopPredicate=CurView.AllSortedPredicates.getValueAt(0, 1).toString();
           
           
        this.MergedSimilirty=CurView.NearestTargetSim;
        int i=1;
                while(this.MergedSimilirty<=CurView.NearestTargetSim && i<CurView.AllSortedPredicates.getRowCount()){
                
                    // Merge
                    XTR_FastPredicate MergedPredicate= new XTR_FastPredicate();
                    MergedPredicate.View=CurView;
                    MergedPredicate.M=CurView.VMD;
                    
                    String PredicateWHER=CurView.AllSortedPredicates.getValueAt(i, 1).toString();
                    XTR_FastPredicate TOPPredicate= FindFastPredicate(CurView.TopPredicate);
                    XTR_FastPredicate Predicate= FindFastPredicate(PredicateWHER);
                    
                   // Merge only Predicates have Target Similirity 
                    if(!Double.isNaN(Predicate.TargetSim))
                    {     
                    //Get right Condition i.e. remove duplications 
                    
                    MergedPredicate.PredicateWehe= MergedPredicate.ModifyMergedPedicates(TOPPredicate, Predicate);
                    MergedPredicate.PredName="External Merged Predicate";
                    
                    if( !MergedPredicate.PredicateWehe.replaceAll(" ", "").equalsIgnoreCase(TOPPredicate.PredicateWehe.replaceAll(" ", ""))) {
                    MergedPredicate.ComputeTragetSimilirity();
                    this.MergedSimilirty=MergedPredicate.TargetSim;
                    MergedPredicate.InternalSim=Double.NaN;
                    
                    System.out.println(MergedPredicate.PredName+"|\t"+MergedPredicate.PredicateWehe+"|\t"+MergedPredicate.TargetSim); 
                    
                             if(MergedPredicate.TargetSim<=CurView.NearestTargetSim)
                                    {
                                        CurView.TopPredicate=MergedPredicate.PredicateWehe;
                                        CurView.NearestTargetSim=MergedPredicate.TargetSim;

                                      

                                    }
                             
                               // Store New Merged Predicate;
                                        CurView.ViewPredicates.add(MergedPredicate);
                                        Vector PredStatInf= new Vector();
                                                        PredStatInf.add(MergedPredicate.PredName);
                                                        PredStatInf.add(MergedPredicate.PredicateWehe);
                //                                        PredStatInf.add(P.NoRecods);
                //                                        PredStatInf.add(P.ValueAfterDel);
                //                                        PredStatInf.add(P.Influence);
                                                        PredStatInf.add(MergedPredicate.InternalSim);
                                                        PredStatInf.add(MergedPredicate.TargetSim);
                                                        
                                        CurView.AllSortedPredicates.addRow(PredStatInf);
                                        BestMergedCAtt_S1(MergedPredicate);
                                        BestMergedRAtt(MergedPredicate);
                                        
                                 //   Vector AllPredicates_data = CurView.AllSortedPredicates.getDataVector();
                                 //   Collections.sort(AllPredicates_data, new ColumnSorter(3, true));
                    }
                }
//                    else
//                        System.out.println("ALready Merged Before !!!!!!!");
                    i++;
                    
                    
                }
                    
              
                    
                    //Sort Neasest distance to target 
                                    Vector AllPredicates_data = CurView.AllSortedPredicates.getDataVector();
                                    Collections.sort(AllPredicates_data, new ColumnSorter(3, true));
    }
  
   
   public void BestMergedCAtt_S1(XTR_FastPredicate ModifiedPredicate){
        
        //String ExeculdedAttr=Predicate1+" " +Predicate2+this.CurView.AggAtt+this.CurView.GrbyAtt+this.CurView.WhrCond;
        String ExeculdedAttr=ModifiedPredicate.PredicateWehe+" " +this.CurView.AggAtt+" "+this.CurView.WhrCond;
        if (CurView.MaxSpit_CountDistinct==CurView.TableSizeV)
            ExeculdedAttr=ModifiedPredicate.PredicateWehe+" " +this.CurView.AggAtt+" "+this.CurView.GrbyAtt+" "+this.CurView.WhrCond;
        try{
            Map<String, String> columnAttributes = Md.getDimColumn();
            //Predicate1=ReversePredicate( Predicate1);
            //Predicate2=ReversePredicate( Predicate2);
            for (Map.Entry<String, String> columnAttribute1 : columnAttributes.entrySet()){
		
			String attr = columnAttribute1.getKey().toString();
                         if (!ExeculdedAttr.contains(attr)){
                             
                 
                             String NewPred= ModifiedPredicate.PredicateWehe;
                                 //   NewPred=ReversePredicate(NewPred);
                                    
                             if (this.CurView.WhrCond.length()>4)
                                 NewPred=this.CurView.WhrCond+" "+ModifiedPredicate.PredicateWehe;
                             else
                                 NewPred=ModifiedPredicate.PredicateWehe.toLowerCase().replaceFirst(" and ", " where ");
                             
                             // return values only e.g. 'a','b','c'
                             NewPred=ReversePredicate(NewPred);
                             String PredValues =Md.getDistinctValuesWhr(attr,NewPred);
                             
                                  
                                   
//                                  String PredValues= getBestAttrMergCluase(attr,P1_results);
                                  String  BestCluase= " and "+attr+" NOT IN ("+PredValues+")";
                                                
                                        int NoofRec=0;
                                        if (this.CurView.WhrCond.length()>4)
                                            NoofRec=Md.getNumRowsWhre( this.CurView.WhrCond+" "+BestCluase);
                                        else
                                            NoofRec=Md.getNumRowsWhre(BestCluase.toLowerCase().replaceFirst(" and ", " where "));
                                        //&& !IsMergedBefore(BestCluase)
                        if(NoofRec>0 ){
                                        XTR_FastPredicate MergedPredicate= new XTR_FastPredicate();
                                        MergedPredicate.M=this.Md;
                                        MergedPredicate.View=this.CurView;
                                        MergedPredicate.PredName="Best Clause";
                                        MergedPredicate.PredicateWehe=BestCluase;
                                        MergedPredicate.NoRecods=NoofRec;
                                        MergedPredicate.Store_PredAtts(attr, "NOT IN", PredValues, "C");
                                        
                                        // Evaulate Predicate
                                         if (this.CurView.CurrMetic==XTR_FastView.XTR_FastMetric.PU_l2_norm){
                                             MergedPredicate.ComputeTragetSimilirity();
                                             if(this.CurView.Print)
                                             System.out.println(MergedPredicate.PredName+"\t"+MergedPredicate.PredicateWehe+"\t"+MergedPredicate.NoRecods+"\t"+"\t"+MergedPredicate.TargetSim);
                                         }
                                         else if (this.CurView.CurrMetic==XTR_FastView.XTR_FastMetric.PU_Corr_distnace){
                                             MergedPredicate.ComputeTragetCorr();
                                             //P.ComputeTragetPCorrDis();
                                             if(this.CurView.Print)
                                             System.out.println(MergedPredicate.PredName+"\t"+MergedPredicate.PredicateWehe+"\t"+MergedPredicate.NoRecods+"\t"+"\t"+MergedPredicate.TargetSim);
                                         }
                                         else if (this.CurView.CurrMetic==XTR_FastView.XTR_FastMetric.PI_Influence){
                                             MergedPredicate.ComputeInflunce();
                                             if(this.CurView.Print)
                                             System.out.println(MergedPredicate.PredName+"\t"+MergedPredicate.PredicateWehe+"\t"+MergedPredicate.NoRecods+"\t"+"\t"+MergedPredicate.TargetSim);
                                         }
                                            
                                        
                                      //  MergedPredicate.PredAddinfo( AllPredicates);
                                         MergedPredicate.InternalSim=Double.NaN;
                                         
                                        if(MergedPredicate.TargetSim<=CurView.NearestTargetSim){
                                            
                                        this.CurView.NearestTargetSim=MergedPredicate.TargetSim;
                                       
                                        this.CurView.TopPredicate=BestCluase;
                                     //   this.CurView.CurrentTargetSim=MergedPredicate.TargetSim;
                                        
                                        
                                       
                                        }
                                        
                                         Vector PredStatInf= new Vector();
                                                        PredStatInf.add(MergedPredicate.PredName);
                                                        PredStatInf.add(MergedPredicate.PredicateWehe);
                                                        PredStatInf.add(MergedPredicate.InternalSim);
                                                        PredStatInf.add(MergedPredicate.TargetSim);
                                                        
                                        this.CurView.AllSortedPredicates.addRow(PredStatInf);
                                        this.CurView.ViewPredicates.add(MergedPredicate);
                                     }
                                     
                        
                                        //if lower should break
                         }
                         
            }
        
        } catch(Exception e){
        e.printStackTrace();
        }
    }
   
   public void BestMergedRAtt(XTR_FastPredicate ModifiedPredicate){
        
        String ExeculdedAttr=ModifiedPredicate.PredicateWehe+" " +this.CurView.AggAtt+this.CurView.WhrCond;
        if (CurView.MaxSpit_CountDistinct==CurView.TableSizeV)
            ExeculdedAttr=ModifiedPredicate.PredicateWehe+" " +this.CurView.AggAtt+this.CurView.WhrCond+this.CurView.GrbyAtt;
        
        //String ExeculdedAttr=ModifiedPredicate.PredicateWehe+" " +this.CurView.AggAtt+" "+this.CurView.GrbyAtt+" "+this.CurView.WhrCond;
        
        try{
    
          Map<String, String> RangAttributes = Md.getMeasureColumn();
//         l1 < l2 ? -1 : 1
         
           String Rang="";
//          Predicate1=ReversePredicate( Predicate1);
//          Predicate2=ReversePredicate( Predicate2);
          
          for (Map.Entry<String, String> columnAttribute1 : RangAttributes.entrySet()){
		
			String Rangattr = columnAttribute1.getKey().toString();
                        if (!ExeculdedAttr.contains(Rangattr)){
                            
                                 String CombinedPredicates1=ReversePredicate(ModifiedPredicate.PredicateWehe);
                                
                                 if (this.CurView.WhrCond.length()>4)
                                     CombinedPredicates1=this.CurView.WhrCond+" "+ModifiedPredicate.PredicateWehe;
                                 else
                                     CombinedPredicates1=ModifiedPredicate.PredicateWehe.toLowerCase().replaceFirst(" and ", " where ");
                                 
                        CombinedPredicates1=ReversePredicate(CombinedPredicates1);
                        double P1_lowV=Md.getMin(Rangattr,CombinedPredicates1);
                        double P1_highV=Md.getMax(Rangattr,CombinedPredicates1);
                       
         
                        int NoofRec=0;
                        Rang=" and "+Rangattr+" NOT BETWEEN "+Math.min(P1_lowV, P1_highV) +" AND "+Math.max(P1_lowV, P1_highV); 
                        if(CurView.WhrCond.length()>4)
                                {
                                    NoofRec=Md.getNumRowsWhre(CurView.WhrCond+" "+Rang);
                              
                                 
                                } 
                        else
                            NoofRec=Md.getNumRowsWhre(Rang.toLowerCase().replaceFirst(" and ", " where "));
                         
                        //&& !IsMergedBefore(Rang)
                        if(NoofRec>0 ){
                        XTR_FastPredicate MergedPredicate= new XTR_FastPredicate();
                                        MergedPredicate.M=this.Md;
                                        MergedPredicate.View=this.CurView;
                                        MergedPredicate.PredName="Best Range";
                                        MergedPredicate.PredicateWehe=Rang;
                                        MergedPredicate.NoRecods=NoofRec;
                                        
                                        MergedPredicate.Store_PredAtts(Rangattr, " NOT BETWEEN ", Math.min(P1_lowV, P1_highV) +" AND "+Math.max(P1_lowV, P1_highV), "R");
                                        // Evaulate Predicate
                                         if (this.CurView.CurrMetic==XTR_FastView.XTR_FastMetric.PU_l2_norm){
                                             MergedPredicate.ComputeTragetSimilirity();
                                             if(this.CurView.Print)
                                             System.out.println(MergedPredicate.PredName+"\t"+MergedPredicate.PredicateWehe+"\t"+MergedPredicate.NoRecods+"\t"+"\t"+MergedPredicate.TargetSim);
                                         }
                                         else if (this.CurView.CurrMetic==XTR_FastView.XTR_FastMetric.PU_Corr_distnace){
                                             MergedPredicate.ComputeTragetCorr();
                                             //P.ComputeTragetPCorrDis();
                                             if(this.CurView.Print)
                                             System.out.println(MergedPredicate.PredName+"\t"+MergedPredicate.PredicateWehe+"\t"+MergedPredicate.NoRecods+"\t"+"\t"+MergedPredicate.TargetSim);
                                         }
                                         else if (this.CurView.CurrMetic==XTR_FastView.XTR_FastMetric.PI_Influence){
                                             MergedPredicate.ComputeInflunce();
                                             if(this.CurView.Print)
                                             System.out.println(MergedPredicate.PredName+"\t"+MergedPredicate.PredicateWehe+"\t"+MergedPredicate.NoRecods+"\t"+"\t"+MergedPredicate.TargetSim);
                                         }
                                        
                                        MergedPredicate.InternalSim=Double.NaN;
                                        
                                        if(MergedPredicate.TargetSim<=CurView.NearestTargetSim){
                                                    CurView.NearestTargetSim=MergedPredicate.TargetSim;
                                                    CurView.TopPredicate=Rang;
                                       
                                        }
                                        
                                         Vector PredStatInf= new Vector();
                                                        PredStatInf.add(MergedPredicate.PredName);
                                                        PredStatInf.add(MergedPredicate.PredicateWehe);
                                                        PredStatInf.add(MergedPredicate.InternalSim);
                                                        PredStatInf.add(MergedPredicate.TargetSim);
                                                        
                                        this.CurView.AllSortedPredicates.addRow(PredStatInf);
                                        this.CurView.ViewPredicates.add(MergedPredicate);
                        }
                        
                        
                        }
                }
        }catch( Exception e){
                e.printStackTrace();
        }
          
    }
 
   public boolean IsMergedBefore(String Predicate){
        boolean f=false;
        
        for(int i=0;i<this.CurView.AllSortedPredicates.getRowCount();i++){
            String MergedP=this.CurView.AllSortedPredicates.getValueAt(i, 1).toString();
            if(MergedP.contains(Predicate)){
                return f=true;
            }
        }
    return f;
    }
    
   
    public XTR_FastPredicate FindFastPredicate(String PredName){
    
        XTR_FastPredicate Predicate=new XTR_FastPredicate();
        for(XTR_FastPredicate NaivePredicate : CurView.ViewPredicates){
            
            String CurPredName=NaivePredicate.PredicateWehe.toString();
            
            if(CurPredName.equalsIgnoreCase(PredName))
            {
                Predicate=NaivePredicate;
                return NaivePredicate;
            }
        }
            return Predicate;
    }
    
    
    public <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();

        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }
    
    public <T> int intersection_Count(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();
            int i=0;
        for (T t : list1) {
            if(list2.contains(t)) {
               // list.add(t);
                i=i+1;
            }
        }

        return i;
    }
    
    public <T> List<T> union1(List<T> list1) {
        
         Set<T> set = new HashSet<T>();

        set.addAll(list1);
      //  set.addAll(list2);

        return new ArrayList<T>(set);

      
    }
    
    public String getBestAttrMergCluase( String Attr,List<String>  Merged_results){
       //int i=0;
       
        String Clause=" and "+Attr+" not in (";
        for (String temp : Merged_results) {
            Clause=Clause+"'"+temp+"',";
			//System.out.println(temp);
		}
        return Clause=Clause.substring(0,Clause.length()-1)+")";
    }
    
    public String ReversePredicate(String Predicate){
        
        Predicate= Predicate.replaceAll("<>", " = ");
        Predicate=Predicate.toLowerCase().replaceAll("not", "");
        return Predicate;
    }
    
    
}
