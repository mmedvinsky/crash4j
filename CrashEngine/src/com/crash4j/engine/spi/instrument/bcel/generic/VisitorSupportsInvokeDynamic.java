package com.crash4j.engine.spi.instrument.bcel.generic;

public interface VisitorSupportsInvokeDynamic extends Visitor{

	void visitNameSignatureInstruction(NameSignatureInstruction obj);
	void visitINVOKEDYNAMIC(INVOKEDYNAMIC obj);
}
