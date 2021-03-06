package org.dianahep.root4j.reps;

import org.dianahep.root4j.RootClassNotFound;
import org.dianahep.root4j.core.AbstractRootObject;
import org.dianahep.root4j.core.CloneLeaf;
import org.dianahep.root4j.core.GenericRootClass;
import org.dianahep.root4j.core.HollowBuilder;
import org.dianahep.root4j.core.RootClassFactory;
import org.dianahep.root4j.core.RootInput;
import org.dianahep.root4j.core.SingleLeaf;
import org.dianahep.root4j.interfaces.TBranch;
import org.dianahep.root4j.interfaces.TBranchClones;
import org.dianahep.root4j.interfaces.TLeaf;
import org.dianahep.root4j.interfaces.TObjArray;

import java.io.IOException;


/**
 *
 * @author tonyj
 * @version $Id: TBranchClonesRep.java 8584 2006-08-10 23:06:37Z duns $
 */
public abstract class TBranchClonesRep extends AbstractRootObject implements TBranchClones
{
   private Class hollowClass;
   private RootInput rin;
   private String fClassName;
   private String fName;
   private String fTitle;
   private TBranch fBranchCount;
   private TObjArray fBranches;
   private TObjArray fLeaves;
   private double fEntries;
   private double fTotBytes;
   private double fZipBytes;
   private int fBasketSize;
   private int fBits;
   private int fCompress;
   private int fEntryNumber;
   private int fEntryOffset;
   private int fMaxBaskets;
   private int fOffset;
   private int fUniqueId;
   private int fWriteBasket;

   public TObjArray getLeaves()
   {
      return fLeaves;
   }

   //public Object getEntry(int index) throws IOException
   //{
   //   int l = ((Integer) count.getEntry(index)).intValue();
   //   return new HollowArray(l,index,objectClass,this);
   //}
   public Class getObjectClass()
   {
      try
      {
         if (hollowClass == null)
         {
            HollowBuilder builder = new HollowBuilder(this, true);
            String name = "org.dianahep.root4j.hollow." + getClassName();
            RootClassFactory factory = rin.getFactory();
            GenericRootClass gc = (GenericRootClass) factory.create(getClassName());
            hollowClass = factory.getLoader().loadSpecial(builder, name, gc);

            // Populate the leafs.
            builder.populateStatics(hollowClass, factory);
            return hollowClass;
         }
         return hollowClass;
      }
      catch (RootClassNotFound x)
      {
         throw new RuntimeException("Error looking up class for TBranchClones " + x.getClassName(),x);
      }
   }

   public void readMembers(RootInput in) throws IOException
   {
      int v = in.readVersion(this);
      v = in.readVersion(this);
      v = in.readVersion(this);

      fUniqueId = in.readInt();
      fBits = in.readInt();

      fName = in.readObject("TString").toString();
      fTitle = in.readObject("TString").toString();

      fCompress = in.readInt();
      fBasketSize = in.readInt();
      fEntryOffset = in.readInt();
      fMaxBaskets = in.readInt();
      fWriteBasket = in.readInt();
      fEntryNumber = in.readInt();
      fEntries = in.readDouble();
      fTotBytes = in.readDouble();
      fZipBytes = in.readDouble();
      fOffset = in.readInt();
      fBranchCount = (TBranch) in.readObjectRef();
      fClassName = in.readObject("TString").toString();
      fBranches = (TObjArray) in.readObject("TObjArray");

      rin = in.getTop();

      TLeaf leaf = new CloneLeaf();
      leaf.setBranch(this);
      fLeaves = new SingleLeaf(leaf);
   }
}
