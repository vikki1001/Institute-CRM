package com.ksv.ktrccrm.constant;

import javax.servlet.http.HttpServletRequest;

public final class ProdConstant {

	public static final String TRUE = "1";	
	public static final String FALSE = "0";
	
	public static final Integer ISACTIVE = 1;	
	public static final Integer INACTIVE = 0;
	
	public static final String ALL = "All";
	public static final String SELECT = "Select";
	public static final String EMPTY = "";
	public static final String FLAG_Y = "Y";
	public static final String FLAG_N = "N";
	
	public static final String EMPDEPARTMENT = "Employee";
	public static final String ORGDEPARTMENT = "Organization";	
	public static final String COMPLETED = "Completed";
	public static final String MARRIED = "Married";
	public static final String PENDING = "Pending";
	public static final String APPROVED = "Approved";
	public static final String REJECTED = "Rejected";
	public static final String UNREAD = "Unread";
	public static final String READ = "Read";
	public static final String ABSENT = "Absent";
	public static final String PRESENT = "Present";
	public static final String DRAFT = "Draft";
	public static final String TOBEPROVIDED = "To Be Provided";
	
	public static final Object TOTALLEAVE = "Total Leave";
	public static final Object PAIDLEAVE = "Paid Leave";	
	
	public static final String HR = "HR";
	
	public static final String LEAVE_REQUEST = "Leave Request";
	public static final String LEAVE_APPROVED = "Leave Approved";
	public static final String LEAVE_REJECT = "Leave Reject";
	public static final String EXITACTIVITY_REQUEST = "ExitActivity Request";
	public static final String EXITACTIVITY_APPROVED = "ExitActivity Approved";
	public static final String EXITACTIVITY_REJECT = "ExitActivity Reject";
	public static final String EXPENSEREIMB_REQUEST = "ExpenseReimb Request";
	public static final String EXPENSEREIMB_APPROVED = "ExpenseReimb Approved";
	public static final String EXPENSEREIMB_REJECT = "ExpenseReimb Reject";
	public static final String ADDNEWREQ_REQUEST = "AddNewReq Request";
	public static final String ADDNEWREQ_APPROVED = "AddNewReq Approved";
	public static final String ADDNEWREQ_REJECT = "AddNewReq Reject";
	public static final String TRAININGFORMREQ = "Training Form Req";
	public static final String SELFAPPRAISAL = "Self Appraisal";
	public static final String RATINGBYREPORTINGMANAGER = "Rating by Reporting Manager";
	public static final String RATINGBYMANAGER = "Rating by Manager";
	public static final String CUSTOMERCONTACTMAIL = "Customer Contact";
	
	public static final String HALFDAY = "08:00 Hrs";
	public static final String LATEDAY = "10:30:00";
	
	// Login Attempt
	public static final Integer MAX_FAILED_ATTEMPTS = 3;
	
	/* Two Factor Authentication By using Email Id) */
	public static final long OTP_VALID_DURATION = 5 * 60 * 1000; // 5 minutes
	
	public static final String DATEFORMATE = "yyyy-MM-dd";
	public static final String DATETIMEFORMATE = "yyyy-MM-dd HH:mm:ss";
	public static final String DATETIMEFORMATE2 = "yyyy-MM-dd'T'HH:mm";
	public static final String DAY = "EEEE";
	
	public static final String  MONTHFORMAT = "MM";
	public static final String  YEARFORMAT = "YYYY";

	/* Document Format */
	public static final String PNG = "PNG";
	public static final String PNGSMALL = "png";
	public static final String PNGEXTENSION = ".png";
	public static final String EXCELEXTENSION = ".xlsx";
	public static final String PDFEXTENSION = ".pdf";
	
	public static final String EXCELFILEFORMAT = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	public static final String WORDFILEFORMAT = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	public static final String PPTFILEFORMAT = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
	public static final String TEXTFILEFORMAT = "text/plain";
	public static final String PDFFILEFORMAT = "application/pdf";
	public static final String IMAGEFILEFORMAT = "image/jpeg";
	
	public static final String IMAGEFORMATE = "image/jpeg, image/jpg, image/png, image/gif";
	public static final String DOCUMENTFORMATE = "document/jpeg, document/jpg, document/png, document/gif, document/pdf, document/txt, document/docx, document/xlsx";
	
	
	
	public static final String IMAGE_LOCATION = "src/main/resources/static/images/";
	
	public static final String QRCODE = "qrcode/";

	
	public static final String getClientIp(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }
			
	/* Holiday Service */
	public static final String HOLIDAY = "Holiday";
	public static final String HOLIDAYCODE = "HolidayCode";
	public static final String DESCRIPTION = "Description";
	public static final String HOLIDAYDATE = "HolidayDate";
	
	/* Payroll Service */
	public static final String PAYROLL = "Payroll";
	public static final String EMPID = "EmpId";
	public static final String NAMEOFEMP = "NameOfEmp";
	public static final String DESIGNATION = "Designation";
	public static final String WORKINGDAYS = "WorkingDays";
	public static final String BASICSALARY = "BasicSalary";
	public static final String DA = "DA";
	public static final String CONVEYANCE = "Conveyance";
	public static final String HOUSERENT = "HouseRent";
	public static final String MEDICALBENEFIT = "MedicalBenefit";
	public static final String OVERTIMEHOURS = "OverTimeHours";
	public static final String OVERTIMERATE = "OverTimeRate";
	public static final String VARIABLEPAY = "VariablePay";
	public static final String GROSSSALARY = "GrossSalary";
	public static final String INCOMETAX = "IncomeTax";
	public static final String EPF = "EPF";
	public static final String NOOFLEAVES = "NoOfLeaves";
	public static final String LEAVESRATE = "LeavesRate";
	public static final String TOTALDEDUCTION = "TotalDeduction";
	public static final String NETSALARY = "NetSalary";
	
	/* QRCode */ 
	public static final String NAME = "Name -- ";
	public static final String BLOODGROUP = "Blood Group -- ";
	public static final String PRESENTADDRESS = "Present Address -- ";
	public static final String PERMANENTADDRESS = "Permanent Address -- ";
	public static final String EMERGENCYCONTACTNO = "Emergency Contact No -- ";
	
	/* Audit */
	public static final String BROWSER = "Browser";
	public static final String MOBILE = "Mobile";
	public static final String TABLET = "Tablet";
	public static final String TIMEZONE = "Asia/Kolkata";
	public static final String ADMIN = "Admin";
	public static final String BIRTHDAY = "Birthday";
	
	/* Organization */
	public static final String ORGANIZATION = "Organization";
	public static final String STUDENTS = "Students";
	public static final String STUDENTSBRANCH = "Students Branch";
	public static final String ORGANIZATIONEMPLOYEE = "Organization Employee";
	public static final String SUCCESS = "Success";
	public static final String FAILED = "Failed";
	
	public static final long CHECKDAY = 30;
	
	public static final String PAYSLIP = "(Note: This is a system generated Pay slip & does not required signature)";
	public static final String ORGANIZATIONCONTACT = "OR";
	public static final String ORGANIZATIONCONTACTEMPLOYEE = "ORE";
	public static final String CUSTOMERCONTACT = "ST";
	
	
	
	public static final String EMAIL_REGEX = "^[a-z][a-zA-Z0-9_.]*(\\.[a-zA-Z][a-zA-Z0-9_]*)?@[a-z][a-zA-Z-0-9]*\\.[a-z]+(\\.[a-z]+)?$";
	public static final String PHONE_REGEX = "^[0-9]{10}$";
	public static final String STRING_PATTERN = "^[a-zA-Z](|[a-zA-Z]){3,18}[a-zA-Z]$";
	
	
	public static final String CUSTOMEREXCELNAME = "customer_contact_";
	public static final String ORGCONTACTEXCELNAME = "organization_contact_";
	public static final String ORGEMPCONTACTEXCELNAME = "organization_employee_contact_";
	public static final String NINE = "9";
	
	/* Document/File Download Name */
	public static final String ASSESTSFILE = "assests";
	public static final String HOLIDAYFILE = "holiday";
	public static final String PAYROLLFILE = "payroll";
	
	/* Two Factor Authentication By using Email Id) */
	public static final String OTPSEND="otpSend";
	public static final String OTPSUCCESS = "otpSuccess";
	public static final String  WRONGOTP= "otp";
	public static final String PWDNOTMATCH = "pwdNotMatch";
	public static final String PASSWORD = "password";
	public static final String FULL = "full";

	public static final double totalWorkingTimeConvertInSec = 32400; // 9 hours convert into seconds

	/* SMS Record */
	
	
}
