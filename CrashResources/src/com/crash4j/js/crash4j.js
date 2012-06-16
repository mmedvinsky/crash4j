/**
 * Check if the array hs the specific value in it
 * @param arr
 * @param v
 * @returns {Boolean}
 */
function arrayHasValue(arr, v)
{
	for (var exl in arr)
	{
		if (arr[exl] == v)
		{
			return false;	
		}
	}
	return true;
}

/**
 * Convert in data from test into an object that is ready to be rendered
 * by the subsystems subsystem
 *  
 */
function toGraphData(indata) 
{
	var mintm = 0;
	var data = {};
	for (i = 0; i < indata.length; i++)
	{
		var el = indata.splice(i, 1)[0];
		if (data[el.id] == undefined) 
		{
			
			data[el.id] = new Object();
			data[el.id].name = el.name;
			data[el.id].actions = new Object();
		}
		if (el.stat == undefined)
		{
			continue;
		}
		var actions = Object.keys(el.stat);
		
		
		for (var action in actions)
		{ 
			var aa = actions[action];
			if (data[el.id].actions[aa] == undefined)
			{
				data[el.id].actions[aa] = new Object();
				data[el.id].actions[aa].stats = new Array();
				data[el.id].actions[aa].labels = new Array();
			}
			
			data[el.id].actions[aa].labels = ["Timeline", "Test Time", "Crash Timer", "Crash Errors"]
			
			var r = new Array();
			r.push(new Date(el.stat[aa].time.timestamp));
			r.push(el.stat[aa].actualtime.avg);
			r.push(el.stat[aa].time.avg);
			if (el.stat[aa].error)
			{
				r.push(el.stat[aa].error.avg);
			}
			else
			{
				r.push(0);
			}
			data[el.id].actions[aa].stats.push(r);
		}		
	}
	return data;
}

/**
 * Calculate graph view
 */
function createGraphView(data, parent, width, height)
{
	var ids = Object.keys(data);
	for (var id in ids)
	{
		document.createElement('div');
		var xid = ids[id];
		var newdiv = document.createElement('div'); 
		newdiv.setAttribute('id', xid); 
		if (width) 
		{
			newdiv.style.width = width; 
		} 
		if (height) 
		{ 
			newdiv.style.height = 50; 
		}
		newdiv.innerHTML = "<b>" + data[xid].name + "</b>";
		parent.appendChild(newdiv);
		
		var actions = Object.keys(data[xid].actions);
		
		for (var acts in actions)
		{
			var xact = actions[acts];
			var newdiv2 = document.createElement('div'); 
			newdiv2.setAttribute('id', xid+"-"+xact); 
			
			if (width) 
			{
				newdiv2.style.width = width; 
			} 
			if (height) 
			{ 
				newdiv2.style.height = 50; 
			}
			newdiv2.innerHTML = "<i>" + xact + "</i>";
			parent.appendChild(newdiv2);
			
			var gid = xid+"-"+xact+"-graph";
			var newdiv3 = document.createElement('div'); 
			newdiv3.setAttribute('id', gid); 
			
			if (width) 
			{
				newdiv3.style.width = width; 
			} 
			if (height) 
			{ 
				newdiv3.style.height = height; 
			}
			
			parent.appendChild(newdiv3);
			var a = new Dygraph(document.getElementById(gid), 
					data[xid].actions[xact].stats,
			        data[xid].actions[xact]
			        );
			
		}
	}
}
