Design Approach : 
To Credit Decision engine, I am using Spring Boot. Using the Spring REST controller, the required end point has been exposed. It is a model driven design where we have one model as Applicant Details.

I have exposed post call to create applicant details about the applied date, loan status, sanctioned amount. 

I have created a services which provides implementstion below methods.
canCheckCreditScore() is boolean method to check applicant is eligible to check credit score, he gets eligibility if he hasn't checked credit score in last 30 days.
callCreditScoreEngine() is a RestTemplate call to CreditScoreEngine service by providing SSNID.
sanctionLoan() it calculates the sanctioned loan amount if credit score obtained from CreditScoreEngine service is greater than 700.

Exception Handling : 
I have created a custom exception to manage the scenario in the application. All the errors will go through a ControllerAdvice. Which then transforms the exception into "application/vnd.error". Some of the examples are

CreditCheckException - SERVICE_UNAVAILABLE
This will be thrown when applicant with SSN number is trying to check credit score and has already checked within last 30 days.


Validation:
I have tried to put basic Javax validation onto the entities/models.


Security
API security has been implemented with spring basic security.



