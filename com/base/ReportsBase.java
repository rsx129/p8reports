package com.base;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportsBase
{
    public static final String dateStr = new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
	public static final String reportsDir = "reports";
    public final File reportDir = new File(reportsDir);

}
