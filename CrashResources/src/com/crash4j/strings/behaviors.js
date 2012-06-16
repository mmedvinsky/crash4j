{
	"sa": {"name" : "Static Absolute", "desc" : "Static absolute behavior treats submitted values as static literal.  "},
	"sr": {"name" : "Static Relative", "desc" : "Static relative behavior will execute value = current >= absolute ? (no modification) : abs(current - absolute)"},
	"wa": {"name" : "Weighted Absolute", "desc" : "Weighted absolute behavior will use provided weight and apply it to current value = (value * weight) "},
	"wr": {"name" : "Weighted Relative", "desc" : "Weighted relative behavior will use provided weight and apply it to current value = current >= (value * weight) ? (no modifications) : (value * weight)"},
	"ra": {"name" : "Stochastic Absolute ", "desc" : "Stochastic absolute behavior will use stochastic method to produce stochastic result controlled by available weight  value = (RANDOM() % weight);"},
	"rr": {"name" : "Stochastic Relative", "desc" : "Stochastic relative behavior will use stochastic method to produce stochastic result controlled by available weight and current.  value  = current >= (RANDOM() % weight) ? (no modifications) : (RANDOM() % weight);"}
};
