package com.crash4j.engine.spi.instrument.bcel.generic;

import com.crash4j.engine.spi.instrument.bcel.classfile.ConstantCP;
import com.crash4j.engine.spi.instrument.bcel.classfile.ConstantNameAndType;
import com.crash4j.engine.spi.instrument.bcel.classfile.ConstantPool;
import com.crash4j.engine.spi.instrument.bcel.classfile.ConstantUtf8;

/**
 * Super class for FieldOrMethod and INVOKEDYNAMIC, since they both have
 * names and signatures 
 *
 * @version $Id: FieldOrMethod.java 1481383 2013-05-11 17:34:32Z dbrosius $
 * @author  <A HREF="mailto:bill.pugh@gmail.com">Bill Pugh</A>
 */
public abstract class NameSignatureInstruction extends CPInstruction {

	private static final long serialVersionUID = 1L;

	public NameSignatureInstruction() {
		super();
	}

	public NameSignatureInstruction(short opcode, int index) {
		super(opcode, index);
	}

	public ConstantNameAndType getNameAndType(ConstantPoolGen cpg) {
	    ConstantPool cp = cpg.getConstantPool();
	    ConstantCP cmr = (ConstantCP) cp.getConstant(index);
	    return  (ConstantNameAndType) cp.getConstant(cmr.getNameAndTypeIndex());
	}
	/** @return signature of referenced method/field.
	 */
	public String getSignature(ConstantPoolGen cpg) {
		ConstantPool cp = cpg.getConstantPool();
	    ConstantNameAndType cnat = getNameAndType(cpg);
	    return ((ConstantUtf8) cp.getConstant(cnat.getSignatureIndex())).getBytes();
	}

	/** @return name of referenced method/field.
	 */
	public String getName(ConstantPoolGen cpg) {
		ConstantPool cp = cpg.getConstantPool();
	    ConstantNameAndType cnat = getNameAndType(cpg);
	    return ((ConstantUtf8) cp.getConstant(cnat.getNameIndex())).getBytes();
	}

}