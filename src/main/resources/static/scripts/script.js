   //page configuration
   class User {
        constructor(id, name, role, sex){
            this.id = id;
            this.name = name;
            this.role = role;
            this.sex = sex;
        }
    }

    class Schedule{
        constructor(Id, IntructorId, ClassId, TimeSlot, WeekSlot, SubjectCode, SubjectDescription){
            this.Id = Id;
            this.InstructorId = IntructorId;
            this.ClassId = ClassId;
            this.TimeSlot = TimeSlot;
            this.WeekSlot = WeekSlot;
            this.SubjectCode = SubjectCode;
            this.SubjectDescription = SubjectDescription;
        }
    }

    var currentUser;
    var userdetails;

    //
    // SESSION HANDLER
    //
    //get current state
    (async function(){
        // eraseCookie('state');
        // eraseCookie('user');
        var state = getCookie('state');
        var user = getCookie('user');

        userdetails = JSON.parse(user);

        console.log(user);

        await cover_curtain();

        if(state == null){ // throw default if no state
            console.log("in null state");
            let style = await getData("GET", "/get-style/login");
                document.querySelector('#default-style').insertAdjacentHTML("afterend", style);

            let template = await getData("GET", "/get-template/login");
                document.querySelector('.curtain').insertAdjacentHTML("afterend", template);

            draw_curtain();
            return;
        }

        await setMainscreen();
        await setUser();
        await nav_init();

        if(state == "dashboard"){
            document.cookie = null;
            await compose_content(0);
        }
        if(state == "schedule"){
            document.cookie = null;
            await compose_content(1);
        }
        if(state == "instructors"){
            document.cookie = null;
            await compose_content(2);
        }
        if(state == "classes"){
            document.cookie = null;
            await compose_content(3);
        }
        if(state == "students"){
            document.cookie = null;
            await compose_content(4);
        }
        if(state == "logs"){
            document.cookie = null;
            await compose_content(1);
        }

        draw_curtain();

    })();

    function setCookie(name,value,days) {
        var expires = "";
        if (days) {
            var date = new Date();
            date.setTime(date.getTime() + (days*24*60*60*1000));
            expires = "; expires=" + date.toUTCString();
        }
        document.cookie = name + "=" + (value || "")  + expires + "; path=/";
    }

    function getCookie(name) {
        var nameEQ = name + "=";
        var ca = document.cookie.split(';');
        for(var i=0;i < ca.length;i++) {
            var c = ca[i];
            while (c.charAt(0)==' ') c = c.substring(1,c.length);
            if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
        }
        return null;
    }

    async function eraseCookie(name) {   
        document.cookie = name +'=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
    }

    //
    // UTILITY FUNCTIONS
    //
    async function getData(METHOD, URL){
        return await $.ajax({ //call to get the style sheet
            type: METHOD,
            url: URL,
            cache: false,
            success: function(data){
                return data;
            }
        });
    }

    async function sendData(url, data){
        const response = await $.ajax({
            type: "POST",
            url: url,
            data: JSON.stringify(data),
            contentType: "application/json",
            success: function(response){
                return response;
            }
        });

        return await response;
    }

    function draw_curtain(){
        const cur = document.querySelector('.curtain').style.left = "-1150px";
    }

    async function cover_curtain(){
        return new Promise((resolve) => {
            const cur = document.querySelector('.curtain').style.left = "0";
            setTimeout(() => {
                $('.container').remove();
                resolve();
            }, 2000);
        });
    }

    function refresh(){
        location.reload();
    }

    //
    // VIEW CONTROLLER FUNCTIONS
    //

    async function logout(){
        await eraseCookie('state');
        await eraseCookie('user');
        location.reload();
    }

    async function final_form(){
        var form = new FormData(document.querySelector('#reg-form'));

        const firstname = form.get('firstname');
        const middlename = form.get('middlename');
        const lastname = form.get('lastname');

        const data = getCookie('register_data');

        const json = JSON.parse(data);
        const username = json['username'];
        const password = json['password'];
        const sex = json['sex'];
        const role = json['role'];

        let rfid = {
            rfid: "0007863499",
            used_by: "Instructor"
        }

        let {status, message} = await getData("GET", "/api/rfid/verify/" + rfid['rfid']);

        if(status == "Error"){
            document.querySelector('#error-placer').innerHTML = message;
            return;
        }

        let user = {
            username,
            password,
            firstname,
            lastname,
            middlename,
            sex,
            role,
            rfid
        }

        status, message  = await sendData("/api/account/create-account", user);
        if(status == "Success"){
           alert(message);
           location.reload();
        }
    }

    async function next_form(){
        var form = new FormData(document.querySelector('#reg-form'));

        const username = form.get('username');
        const password = form.get('password');
        const repass = form.get('repass');
        const sex = form.get("sex");
        const role = form.get("role");

        if(username === "" ||
        password === "" ||
        repass == ""){
            document.querySelector('#error-placer').innerHTML = "Please fill in the form."; //send warning
            return;
        }

        if(password != repass){
            document.querySelector('#error-placer').innerHTML = "Password doesn't match."; //send warning
            return;
        }

        if(password.length < 8){
            document.querySelector('#error-placer').innerHTML = "Password must atleast be 8 characters long.";
            return;
        }

        let {status, message} = await getData("GET","/api/account/verify/" + username);

        if(status == "Used"){
            document.querySelector('#error-placer').innerHTML = message; //send warning
            return;
        }

        let user = {
            username,
            password,
            sex,
            role
        }

        setCookie('register_data', JSON.stringify(user), 1);

        // inject next page
        let next_form = `
        <form id="reg-form">
            <p id="error-placer" class="text-danger" style="margin-bottom: 10px; color: red;"></p>
            <label style="margin-top: 50px">First name:</label>
            <input name='firstname' class='input' type='text' placeholder='First name'>

            <label>Middle name:</label>
            <input name='middlename' class='input' type='text' placeholder='Middle name'>

            <label>Last name:</label>
            <input name='lastname' class='input' type='text' placeholder='Last name'>

            <input style='margin-bottom: 25px;' type='button' value='Submit' onclick='final_form()'>
        </form>`;

        document.querySelector('#reg-form').innerHTML = next_form;
    }

    async function register(){
        console.log("here")
        await cover_curtain();
        let style = await getData("GET", "/get-style/registration");
            // console.log(typeof(style));
            document.querySelector('#login-style').remove();
            document.querySelector('#default-style').insertAdjacentHTML("afterend", style);

        let template = await getData("GET", "/get-template/registration");
            // console.log(template);
            document.querySelector('.curtain').insertAdjacentHTML("afterend", template);

        draw_curtain();
    }

    async function login(){
        console.log("here")
        await cover_curtain();
        let style = await getData("GET", "/get-style/login");
            document.querySelector('#registration-style').remove();
            document.querySelector('#default-style').insertAdjacentHTML

        let template = await getData("GET", "/get-template/login");
            document.querySelector('.curtain').insertAdjacentHTML("afterend", template);
        draw_curtain();
    }

    async function logMeIn(){
        var error = document.querySelector('#error-placer');
        var form = new FormData(document.querySelector('#log-form'));

        const username = form.get('username');
        const password = form.get('password');
    
        // some inputs are empty
        if(username === "" ||
        password === ""){
            console.log('Empty field, please fill!');
            return;
        }
    
        let formData = {
            username,
            password
        };

        let { status, message } = await sendData("/api/account/login-account", formData);
        if (status == "No match") {
            error.innerHTML = message;
            document.querySelector('#username').value = "";
            document.querySelector('#password').value = "";
            document.querySelector('#username').focus();
            return;
        }
        if(status == "Error"){
            error.innerHTML = message;
            document.querySelector('#password').value = "";
            document.querySelector('#password').focus();
            return;
        }

        await cover_curtain();
        userdetails = JSON.parse(message);// save user details

        let userStyle;
        let userTemplate;
        if(userdetails['role'] == "Admin"){
            userStyle = "/get-style/mainscreen-admin";
            userTemplate = "/get-template/mainscreen-admin";
        } else {
            userStyle = "/get-style/mainscreen";
            userTemplate = "/get-template/mainscreen";
        }

        console.log(userStyle)

        setCookie('user', message, 1);
        
        //inject dashboard template
        let style = await getData("GET", userStyle);
            document.querySelector('#login-style').remove();
            document.querySelector('#default-style').insertAdjacentHTML("afterend", style);

        let template = await getData("GET", userTemplate);
            document.querySelector('.curtain').insertAdjacentHTML("afterend", template);
        
        await nav_init();
        await setUser();
        await compose_content(0);

        draw_curtain();
    }

    async function compose_content(id){
        var userCookie = getCookie('user');
        const user = JSON.parse(userCookie);
        const role = user['role'];

        document.querySelector('.floater').style.right = "-290px";

        if(role == "Instructor"){
            if(id == 0){
                document.querySelector('.wrapper').remove();
                if(document.querySelector('#diff-style') != null){
                    document.querySelector('#diff-style').remove();
                }
    
                let style = await getData("POST", "/get-style/dashboard");
                    document.querySelector('#default-style').insertAdjacentHTML("afterend", style);
    
                let template = await getData("POST", "/get-template/dashboard");
                    document.querySelector('.content').innerHTML = template;
    
                    await dashboard_init();
                    setCookie('state','dashboard', 1);
                    setCookie('user', JSON.stringify(userdetails), 1);
            }
    
            if(id == 1){
                document.querySelector('.wrapper').remove();
                if(document.querySelector('#diff-style') != null){
                    document.querySelector('#diff-style').remove();
                }
    
                let style = await getData("POST", "/get-style/schedule");
                document.querySelector('#default-style').insertAdjacentHTML("afterend", style);
    
                let template = await getData("POST", "/get-template/schedule");
                document.querySelector('.content').innerHTML = template;
    
                    await schedule_init();
                    setCookie('state','schedule', 1);
                    setCookie('user', JSON.stringify(userdetails), 1);
            }
    
            if(id == 2){
                document.querySelector('.wrapper').remove();
                document.querySelector('#diff-style').remove();
    
                let style = await getData("POST", "/get-style/classes");
                document.querySelector('#default-style').insertAdjacentHTML("afterend", style);
    
                let template = await getData("POST", "/get-template/classes");
                document.querySelector('.content').innerHTML = template;
    
                classes_init();
            }
        } else {
            if(id == 0){
                document.querySelector('.wrapper').remove();
                if(document.querySelector('#diff-style') != null){
                    document.querySelector('#diff-style').remove();
                }
    
                let style = await getData("POST", "/get-style/dashboard-admin");
                    document.querySelector('#default-style').insertAdjacentHTML("afterend", style);
    
                let template = await getData("POST", "/get-template/dashboard-admin");
                    document.querySelector('.content').innerHTML = template;
    
                    await admin_dashboard_init();
                    setCookie('state','dashboard', 1);
                    setCookie('user', JSON.stringify(userdetails), 1);
            }
            if(id == 1){
                document.querySelector('.wrapper').remove();
                if(document.querySelector('#diff-style') != null){
                    document.querySelector('#diff-style').remove();
                }

                let style = await getData("POST", "/get-style/logs");
                document.querySelector('#default-style').insertAdjacentHTML("afterend", style);
    
                let template = await getData("POST", "/get-template/logs");
                    document.querySelector('.content').innerHTML = template;
    
                    await log_init();
                    setCookie('state','logs', 1);
                    setCookie('user', JSON.stringify(userdetails), 1);
            }
            if(id == 2){
                document.querySelector('.wrapper').remove();
                if(document.querySelector('#diff-style') != null){
                    document.querySelector('#diff-style').remove();
                }
    
                let style = await getData("POST", "/get-style/instructors");
                    document.querySelector('#default-style').insertAdjacentHTML("afterend", style);
    
                let template = await getData("POST", "/get-template/instructors");
                    document.querySelector('.content').innerHTML = template;
    
                    await instructors_init();
                    setCookie('state','instructors', 1);
                    setCookie('user', JSON.stringify(userdetails), 1);
            }
            if(id == 3){
                document.querySelector('.wrapper').remove();
                if(document.querySelector('#diff-style') != null){
                    document.querySelector('#diff-style').remove();
                }
    
                let style = await getData("POST", "/get-style/classes");
                    document.querySelector('#default-style').insertAdjacentHTML("afterend", style);
    
                let template = await getData("POST", "/get-template/classes");
                    document.querySelector('.content').innerHTML = template;
    
                    await classes_init();
                    setCookie('state','classes', 1);
                    setCookie('user', JSON.stringify(userdetails), 1);
            }
            if(id == 4){
                document.querySelector('.wrapper').remove();
                if(document.querySelector('#diff-style') != null){
                    document.querySelector('#diff-style').remove();
                }
    
                let style = await getData("POST", "/get-style/students");
                    document.querySelector('#default-style').insertAdjacentHTML("afterend", style);
    
                let template = await getData("POST", "/get-template/students");
                    document.querySelector('.content').innerHTML = template;
    
                    await student_init();
                    setCookie('state','students', 1);
                    setCookie('user', JSON.stringify(userdetails), 1);
            }
        }
    }

    async function compose_card(objects){
        

        return constructed;
    }

    //
    // SETTERS
    //
    async function setUser(){
        document.querySelector('#user-name').innerHTML = userdetails['firstname'] + " " + userdetails['lastname'];
        document.querySelector('#role').innerHTML = userdetails['role'];
    }

    async function setMainscreen(){
        let userStyle = "";
        let userTemplate = "";

        var user = getCookie('user');
        user = JSON.parse(user);
        const role = user['role'];

        console.log(role);

        if(role == "Instructor"){
            userStyle = "/get-style/mainscreen";
            userTemplate = "/get-template/mainscreen";
        } else {
            userStyle = "/get-style/mainscreen-admin";
            userTemplate = "/get-template/mainscreen-admin";
        }

        let style = await getData("GET", userStyle);
            document.querySelector('#default-style').insertAdjacentHTML("afterend", style);

        let template = await getData("GET", userTemplate);
            document.querySelector('.curtain').insertAdjacentHTML("afterend", template);

        let {status, message} = await getData("GET", "/api/batch/get-batches")

        // let batchContent = ``;

        // if(status == "Empty"){
        //     batchContent = message;
        // } else {
        //     const batches = JSON.parse(message);

        //     for(var i = 0; i < batches.length; i++){
        //         let container = `
        //             <option value=` + batches[i]['id'] + `>` + batches[i]['course'] + " - " + batches[i]['section'] + `</option>
        //         `;
    
        //         batchContent += container;
        //     }
        // }

        // document.querySelector('#batch').innerHTML = batchContent;
    }

    //
    // FEATURE HANDLERS
    //
    function openScheduleForm(){
        document.querySelector('#schedule-float').style.right = "70px";
    }
    function hideScheduleForm(){
        document.querySelector('#schedule-float').style.right = "-290px";
    }

    function showUserSettings(){
        document.querySelector('#user-settings').style.right = "70px";
    }
    function hideUserSettings(){
        document.querySelector('#user-settings').style.right = "-290px";
    }

    async function prepareStudentForm(){
        let {status, message} = await getData("GET", "/api/batch/get-batches");

        let batchOption = ``;
        if(status == "Error"){
            alert(message);
            location.reload();
        }

        if(status == "Empty"){
            batchOption = `
                <option>No batch found</option>
            `;
        } else {
            const batch = JSON.parse(message);

            for(var i = 0; i < batch.length; i++){
                let container = `
                    <option value='` + batch[i]['id'] + `'>` + batch[i]['course'] + " - " + batch[i]['section'] + `</option>
                `;
                batchOption += container;
            }
            console.log(batch);
        }

        let content = `
            <label class="schedule-label">First name: </label>
            <input class="schedule-input" id="firstname" name="firstname" type="text" placeholder="First name"><br>
            <label class="schedule-label">Middle name: </label> 
            <input class="schedule-input" id="middlename" name="middlename" type="text" placeholder="Middle name"><br>
            <label class="schedule-label">Last name: </label>
            <input class="schedule-input" id="lastname" name="lastname" type="text" placeholder="Last name"><br>
            <label class="schedule-label">Batch: </label><br>
            <select id="batch" name="batch">
                `
                    +  batchOption  +
                `
            </select><br>
            <label class="schedule-label">Student ID number: </label>
            <input class="schedule-input" id="studentid" name="studentid" type="text" placeholder="Student ID #"><br>
            <label class="schedule-label">Address </label>
            <input class="schedule-input" id="address" name="address" type="text" placeholder="Address"><br>
            <label class="schedule-label">RFID: </label>
            <input class="schedule-input" id="rfid" name="rfid" type="text" placeholder="Tap RFID"><br>

            <input type="button" value="Create" onclick="registerStudent()">
        `;

        document.querySelector('#student-form').innerHTML = content;
        document.querySelector('#student-modal-title').innerHTML = "Register student";
        document.querySelector('#student-float').style.right = "70px";
    }

    function hideRegisterStudent(){
        document.querySelector('#student-float').style.right = "-290px";
        document.querySelector('#student-error-placer').innerHTML = "";
        document.querySelector('#student-form').reset();
    }

    async function registerStudent(){
        var form = new FormData(document.querySelector('#student-form'));

        const firstname = form.get('firstname');
        const middlename = form.get('middlename');
        const lastname = form.get('lastname');
        const studentid = form.get('studentid');
        const address = form.get('address');
        const batchid ={
            id: form.get('batch')
        } 
        let rfid = {
            rfid: form.get('rfid'),
            used_by: "Student"
        }

        if(firstname == "" || 
        middlename == "" ||
        lastname == "" ||
        studentid == "" ||
        address == "" ||
        rfid == ""){
            document.querySelector('#student-error-placer').innerHTML = "Please fill in the form.";
            return;
        }

        let studentData = {
            firstname,
            lastname,
            middlename,
            studentid,
            address,
            batchid,
            rfid
        }

        let {status, message} = await sendData("/api/student/create-student", studentData);

        if(status == "Error"){
            document.querySelector('#student-error-placer').innerHTML = message;
            return;
        }
        if(status == "Success"){
            //throw success notification
        }
        hideRegisterStudent();
    }

    async function createSchedule(){
        var form = new FormData(document.querySelector('#schedule-form'));

        const subjectname = form.get("subjectname");
        const subjectcode = form.get("subjectcode");
        const subjectdescription = form.get("subjectdesc");
        const start_at = form.get("start_at");
        const end_at = form.get("end_at");
        const id = form.get("batch");
        const weekslot = form.get("weekslot")

        if(subjectname == "" ||
        subjectcode == "" ||
        subjectdescription == ""){
            document.querySelector('#schedule-error-placer').innerHTML = "Please fill in the form.";
            return;
        }

        let batch = {
            id
        }

        let instructor = {
            id : userdetails['id']
        }

        let scheduleData = {
            subjectname,
            subjectcode,
            subjectdescription,
            start_at,
            end_at,
            batch,
            weekslot,
            instructor
        }

        console.log(JSON.stringify(scheduleData));

        let {status, message} = await sendData("/api/schedule/create-schedule", scheduleData);

        if(status == "Success"){
            hideScheduleForm();
            document.querySelector('#schedule-form').reset();
        }

        document.querySelector('#schedule-error-placer').innerHTML = message;
    }

    async function prepareEditStudent(stdid){
        let {status, message} = await getData("GET", "/api/student/get-student/" + stdid);

        if(status == "Error"){
            alert();
            location.reload();
        }

        const student = JSON.parse(message);

        ({status, message} = await getData("GET", "/api/batch/get-batches"))

        if(status == "Error"){
            alert();
            location.reload();
        }

        const batch = JSON.parse(message);

        let batchOption = ``;
        for(var i = 0; i < batch.length; i++){
            let container = `
                <option value='` + batch[i]['id'] + `'>` + batch[i]['course'] + " - " + batch[i]['section'] + `</option>
            `;
            batchOption += container;
        }
        console.log(batch);

        let content = `
            <p id="student-error-placer"></p>
            <input type="hidden" name="id" value='` + student['id'] + `'>
            <input type="hidden" name="rfID" value='` + student['studentRFID']['id'] + `'>
            <label class="schedule-label">First name: </label>
            <input class="schedule-input" id="firstname" name="firstname" type="text" placeholder="First name" value='` + student['firstname'] + `'><br>
            <label class="schedule-label">Middle name: </label> 
            <input class="schedule-input" id="middlename" name="middlename" type="text" placeholder="Middle name" value='` + student['middlename'] + `'><br>
            <label class="schedule-label">Last name: </label>
            <input class="schedule-input" id="lastname" name="lastname" type="text" placeholder="Last name" value='` + student['lastname'] + `'><br>
            <label class="schedule-label">Batch: </label><br>
            <select id="batch" name="batch">
                `
                    +  batchOption  +
                `
            </select><br>
            <label class="schedule-label">Student ID number: </label>
            <input class="schedule-input" id="studentid" name="studentid" type="text" placeholder="Student ID #" value='` + student['studentid'] + `'><br>
            <label class="schedule-label">Address </label>
            <input class="schedule-input" id="address" name="address" type="text" placeholder="Address" value='` + student['address'] + `'><br>
            <label class="schedule-label">RFID: </label>
            <input class="schedule-input" id="rfid" name="rfid" type="text" placeholder="Tap RFID" value='` +  student['studentRFID']['rfid'] + `'><br>

            <input type="button" value="Save" onclick="editStudent()">
        `;

        document.querySelector('#student-modal-title').innerHTML = "Edit student";
        document.querySelector('#student-form').innerHTML = content;
        document.querySelector('#student-float').style.right = "70px";
    }

    async function editStudent(){
        var form = new FormData(document.querySelector('#student-form'));

        const id = form.get('id')
        const firstname = form.get('firstname');
        const middlename = form.get('middlename');
        const lastname = form.get('lastname');
        const studentid = form.get('studentid');
        const address = form.get('address');
        const batchid = {
            id: form.get('batch')
        } 
        let rfid = {
            id: form.get("rfID"),
            rfid: form.get('rfid'),
            used_by: "Student"
        }

        if(
        firstname == "" || 
        middlename == "" ||
        lastname == "" ||
        studentid == "" ||
        address == "" ||
        rfid == ""){
            document.querySelector('#student-error-placer').innerHTML = "Please fill in the form.";
            return;
        }

        let studentData = {
            id,
            firstname,
            lastname,
            middlename,
            studentid,
            address,
            batchid,
            rfid
        }

        let {status, message} = await sendData("/api/student/update-student", studentData);
        console.log(message)
        if(status == "Error"){
            document.querySelector('#student-error-placer').innerHTML = message;
            return;
        }
        if(status == "Success"){
            document.querySelector('#notification-modal').innerHTML = message;
            document.querySelector("#student-float").style.right = "70px";
            document.querySelector('#notification-modal').style.top = "25%";
        }

    }

    async function prepareStudent(stdid){
        console.log(stdid);
        let {status, message} = await getData("GET", "/api/student/get-student/" + stdid);

        if(status == "Error"){
            alert(message);
            location.reload();
        }
        const studentData = JSON.parse(message);

        const schedule = document.querySelector('#schedule-head').dataset.schedule;
        const jsonSchedule = JSON.parse(schedule);
        const scheduleId = jsonSchedule['id'];

        ({status, message} = await getData("GET", "/api/schedule/" + scheduleId + "/get-all-schedules"));
        const schedules = JSON.parse(message);

        let dropdown = ``;
        for(var i = 0; i < schedules.length; i++){
            let container = `
                <option value="` + schedules[i]['id'] + `">` + schedules[i]['subjectcode'] + " : " + schedules[i]['batch']['course'] + " - " + schedules[i]['batch']['section'] + `</option>
            `;
            dropdown += container;
        }

        let transferForm = `
            <p id="transfer-error-placer"></p>
            <input name='exclude-schedule' type='hidden' value='` + scheduleId + `'>
            <input name='studentid' type='hidden' value='` + studentData['id'] + `'>
            <label>Student name</label>
            <input name='studentname' type="text" value='` + studentData['firstname'] + " " + studentData['middlename'] + studentData['lastname'] + `' readonly>
            <label>To schedule: </label>
            <select name="toschedule">
                ` + dropdown + `
            </select><br>
            <label>Note to instructor: </label>
            <textarea id="note" name="note" rows="4" cols="50" style="width: 100%;">
            </textarea>
            <input type="button" value="Transfer" onclick="transferStudent()">
        `;

        document.querySelector('#transfer-form').innerHTML = transferForm;
        document.querySelector('#transfer-float').style.right = "70px";

    } 

    async function transferCancel(){
        document.querySelector('#transfer-float').style.right = "-290px";
        document.querySelector('#transfer-form').reset();
    }

    async function transferStudent(){
        const form = new FormData(document.querySelector('#transfer-form'));
        
        var schedule = form.get("toschedule");
        const student = form.get('studentid');
        var scheduleExcl = form.get('exclude-schedule');

        // var stat = "include";

        // return;

        let studentData = {
            student,
            stat: "include",
            schedule
        }
        console.log(studentData)

        let {status, message} = await sendData("/api/substudent/transfer", studentData);

        if(status == "Error"){
            alert(message);
            location.reload();
        }

        schedule = scheduleExcl;
        console.log(schedule);
        stat = "exclude";

        studentData = {
            student,
            stat: "exclude",
            schedule : scheduleExcl
        }

        console.log(studentData);

        ({status, message} = await sendData("/api/substudent/exclude", studentData));

        if(status == "Error"){
            alert(message);
            location.reload();
        }
        if(status = "Success"){
            alert(message);
            location.reload();
        }

        document.querySelector('#transfer-float').style.right = "-290px";
        document.querySelector('#transfer-form').reset();
    }

    async function prepareExcluding(stdid){
        document.querySelector('#drop-confirmation-modal').dataset.student = stdid;
        let {status, message} = await getData("GET", "/api/student/get-student/" + stdid);


        if(status == "Error"){
            alert(message);
            location.reload();
        }

        const student = JSON.parse(message);
        console.log(student);

        document.querySelector('#student-name').innerHTML = student['firstname'] + " " + student['middlename'] + " " + student['lastname'];
    
        document.querySelector('#drop-confirmation-modal').style.top = "25%";
    }

    async function dropStudent(){
        var schedData = document.querySelector('#schedule-head').dataset.schedule;
        const student = document.querySelector('#drop-confirmation-modal').dataset.student;

        schedData = JSON.parse(schedData);
        const schedule = schedData['id'];

        let stdData = {
            student,
            stat: "exclude",
            schedule
        }

        let{status, message} = await sendData("/api/substudent/exclude", stdData);

        if(status == "Error"){
            alert(message);
            location.reload();
        }

        document.querySelector('#drop-confirmation-modal').style.top = "-290px";
    }

    async function dropCancel(){
        document.querySelector('#drop-confirmation-modal').style.top = "-290px";
    }

    async function renderStudents(e){
        const data = e.dataset.schedule;
        const scheduleData = JSON.parse(data);

        let {status, message} = await getData("GET", "/api/student/get-students/" + scheduleData['batch']['id'] + "/batch/" + scheduleData['id'] + "/schedule");

        if(status == "Error"){
            alert(message);
            location.reload();
        }

        const studentData = JSON.parse(message);

        let tableTemplate = `
            <div class="wrapper">
                <div style="display: flex;">
                    <div style="
                        height: 50px;
                        min-width: 220px;
                        text-align: center;
                        border-top-left-radius: 10px;
                        border-top-right-radius: 10px;
                        line-height: 60px;
                        background-color: #111056;
                    " id="schedule-head" data-schedule='` + JSON.stringify(scheduleData) + `'>
                        <p style="
                        color:  white;
                        "> <span style="font-size: 16px; font-weight: 700;">` + scheduleData['subjectcode'] + `</span> <span style="font-size: 14px;"> | ` + scheduleData['batch']['course'] + " - " + scheduleData['batch']['section'] + `</span> </p>
                    </div>
                    <div style="width: 437px;"></div>
                    <div style="
                        cursor:pointer;
                        color: white;
                        background-color: #111056;
                        min-width: 100px;
                        height: 50px;
                        line-height: 60px;
                        border-top-left-radius: 10px;
                        border-top-right-radius: 10px;
                        text-align: center;
                    " id="strat-cont" onclick="changeStrategy(this, '')" data-strategy='detailed'>Detailed</div>
                </div>
                <table class="styled-table">
                    <thead id="table-head">
                        <tr>
                            <th>#</th>
                            <th>Student name</th>
                            <th>Presents</th>
                            <th>Lates</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody id="table-container">
                    </tbody>
                </table>
            </div>
        `;
        document.querySelector('.content').innerHTML = tableTemplate;

        let content = ``;
        for(var i = 0; i < studentData.length; i++){
            let container = `
                <tr>
                    <td>` + (i+1) + `</td>
                    <td>` + studentData[i]['fullName'] + `</td>
                    <td>` + studentData[i]['present'] + `</td>
                    <td>` + studentData[i]['late'] + `</td>
                    <td>
                    <span><img onclick="prepareStudent(` + studentData[i]['studentId'] +`)" style="width:25px; height: 25px;" src="/scripts/transfer.svg"></span>
                    <span><img onclick="prepareExcluding(`+ studentData[i]['studentId'] + `)" style="width:25px; height: 25px;" src="/scripts/drop.svg"></span>
                    </td>
                </tr>
            `;
            content += container;
        }

        document.querySelector('#table-container').innerHTML = content;
    }

    async function changeStrategy(e, sub){
        var data
        if(e.dataset.strategy != undefined){
            data = e.dataset.strategy;
        } else {
            data = sub;
        }
        const schedData = document.querySelector('#schedule-head').dataset.schedule;
        const scheduleData = JSON.parse(schedData);

        if(data == "detailed"){
            let {status, message} = await getData("GET", "/api/student/get-detailed/" + scheduleData['id'] + "/schedule");

            if(status == "Error"){
                alert(message);
                location.reload;
            }

            const studentData = JSON.parse(message);

            let tableHead = `
                <tr>                            
                    <th>#</th>
                    <th>Student name</th>
                    <th>Date</th>
                    <th>Status</th>
                </tr>
            `;

            let content = ``;
            for(var i = 0; i < studentData.length; i++){
                let container = `
                    <tr>
                        <td>` + (i+1) + `</td>
                        <td>` + (studentData[i]['lastName'] + ", " + studentData[i]['firstName'] + " " + studentData[i]['middleName']) + `</td>
                        <td>` + studentData[i]['dateTime'] + `</td>
                        <td>` + studentData[i]['status'] + `</td>
                    </tr>
                `;
                content += container;
            }
    
            //insert table
            document.querySelector('#table-head').innerHTML = tableHead;
            document.querySelector('#table-container').innerHTML = content;
            document.querySelector('#strat-cont').dataset.strategy = "simple";
            document.querySelector('#strat-cont').innerHTML = "Simple";

        } else {
    
            let {status, message} = await getData("GET", "/api/student/get-students/" + scheduleData['batch']['id'] + "/batch/" + scheduleData['id'] + "/schedule");
    
            if(status == "Error"){
                alert(message);
                location.reload();
            }
    
            const studentData = JSON.parse(message);

            let tableHead = `
                <th>#</th>
                <th>Student name</th>
                <th>Presents</th>
                <th>Lates</th>
                <th>Actions</th>
            `;

            let content = ``;
            for(var i = 0; i < studentData.length; i++){
                let container = `
                    <tr>
                        <td>` + (i+1) + `</td>
                        <td>` + studentData[i]['fullName'] + `</td>
                        <td>` + studentData[i]['present'] + `</td>
                        <td>` + studentData[i]['late'] + `</td>
                        <td>
                            <span><img onclick="prepareStudent(` + studentData[i]['studentId'] +`)" style="width:25px; height: 25px;" src="/scripts/transfer.svg"></span>
                            <span><img onclick="prepareExcluding(`+ studentData[i]['studentId'] + `)" style="width:25px; height: 25px;" src="/scripts/drop.svg"></span>
                        </td>
                    </tr>
                `;
                content += container;
            }

            //insert table content
            document.querySelector('#table-head').innerHTML = tableHead;
            document.querySelector('#table-container').innerHTML = content;
            document.querySelector('#strat-cont').dataset.strategy = "detailed";
            document.querySelector('#strat-cont').innerHTML = "Detailed";
            //revert back to normal
        }
    }

    function showAddScheduleModal(){
        $('#scheduleModal').modal('show');
    }

    function hideNotification(){
        document.querySelector('#notification-modal').style.top = "-290px";
    }

    function prepareClass(){

        let content = `
            <label>Course:</label>
            <input type="text" name="course" placeholder="Course"><br>
            <label>Course:</label>
            <input type="text" name="section" placeholder="Section"><br>

            <input type="button" value="Create" onclick="createClass()"><br>
        `;

        document.querySelector('#universal-header').innerHTML = "Create a class";
        document.querySelector('#universal-form').innerHTML = content;
        document.querySelector('#universal-float').style.right = "70px";
    }

    async function createClass(){
        const form = new FormData(document.querySelector('#universal-form'));

        let batch = {
            course: form.get('course'),
            section : form.get('section')
        }

        let {status, message} = await sendData("/api/batch/create-batch", batch);

        if(status == "Error"){
            document.querySelector('#error-placer').innerHTML = message;
            return;
        }

        document.querySelector('#universal-float').style.right = "-290px";

        document.querySelector('#notification-modal').innerHTML = `
            <h4>` + status + `</h3>
            <p>` + message + `</p>
            <div style="cursor: pointer" onclick="hideNotification()">Done</div>
        `;

        document.querySelector('#notification-modal').style.top = "25%";
    }

    function hideUniversalFloat(){
        document.querySelector('#universal-float').style.right = "-290px";
    }

    async function prepareEditClass(classId){
        let {status, message} = await getData("GET", "/api/batch/get-batch/" + classId);
        console.log(status);
        if(status == "Error"){
            document.querySelector('#notification-modal'). innerHTML = `
                <h4>Error</h4>
                <p>` + message + `</p>
                <p onclick="hideNotification()">Close</p>
            `;
            return;
        }

        const batch = JSON.parse(message);
        console.log(batch);

        let content = `
            <input name="id" type="hidden" value=` + batch['id'] + `>
            <label>Course: </label>
            <input name="course" type="text" value="` + batch['course'] + `" placeholder="Course">
            <label>Course: </label>
            <input name="section" type="text" value="` + batch['section'] + `" placeholder="Course">

            <input type="button" value="Edit" onclick="editClass()">
        `;

        document.querySelector('#universal-header').innerHTML = "Edit class";
        document.querySelector('#universal-form').innerHTML = content;
        document.querySelector('#universal-float').style.right = "70px";

    }

    async function editClass(){
        const form = new FormData(document.querySelector('#universal-form'));

        const id = form.get('id');
        const course = form.get('course');
        const section = form.get('section');

        let batch = {
            id,
            course,
            section
        }

        let {status, message} = await sendData("/api/batch/update-batch", batch);

        if(status == "Error"){
            document.querySelector('#universal-float').style.right = "-290px";
            document.querySelector('#notification-modal').innerHTML = `
                <h4>Error</h4>
                <p>` + message + `</p>
                <p onclick="hideNotification()">Close</p>
            `;
            document.querySelector('#notification-modal').style.top = "25%";
            return;
        }

        document.querySelector('#universal-float').style.right = "-290px";
        document.querySelector('#notification-modal').innerHTML = `
        <h4>` + status + `</h4>
            <p>` + message + `</p>
            <p onclick="hideNotification()">Close</p>
        `;
        document.querySelector('#notification-modal').style.top = "25%";
        compose_content(3);
    }
// 
//    INITIALIZATION AREA 
// 

    async function dashboard_init(){
        const weatherNow = await $.ajax({
            type: "GET",
            url: "http://api.weatherapi.com/v1/current.json?key=090a4b6cd86a439bbe0145533232004&q=philippines&aqi=no",
            success: function(data){
                return data;
            }
        });

        console.log(userdetails['id'] + " hey");

        //get schedules
        let {status, message} = await getData("GET", "/api/schedule/" 
        + userdetails['id'] + "/current-schedule");

        console.log(status + " " + message);

            if(status == "Error"){
                //throw error

                return;
            }

            if(status == "Empty"){
                //throw empty
                document.querySelector('.greeting').innerHTML = `
                <div>
                    <p style="
                    font-size: 25px;
                    font-weight: 700;
                    min-width: 500px;
                    max-width: 500px;
                    ">Welcome, ` + (userdetails["sex"] == "Male" ? "Sir " : "Ma'am ") + userdetails['firstname'] + " " + userdetails['lastname'] + `!</p>
                    <div style="
                    margin-top: 5px;
                    ">
                        <p style="
                        font-size:18px;
                        font-weight: 500;">A greate day to take a rest.</p>
                        <p style="
                        font-size:18px;
                        font-weight: 500;
                        position:relative;
                        top: -10px;">You have no class scheduled for today.</p>
                    </div>
                </div>
                <div style="
                margin-top: 10px; 
                margin-left: 40px; 
                ">            
                    <p style="
                    font-size: 18px;
                    font-weight: 600; 
                    "> ` + weatherNow['location']["localtime"] + ` </p>
                    <p>` + weatherNow["current"]["condition"]["text"] + ` <span style="display:inline-block;"><img style="width:25px; height:25px;" src="` + weatherNow["current"]["condition"]["icon"] + `"></img><span></p>
                </div>`;

                document.querySelector('#schedule-today').innerHTML = `
                    <p> ` + message + ` </p>
                `;

                return;
            } else {
                document.querySelector('.greeting').innerHTML = `
                <div>
                    <p style="
                    font-size: 25px;
                    font-weight: 700;
                    min-width: 500px;
                    max-width: 500px;
                    ">Welcome, ` + (userdetails["sex"] == "Male" ? "Sir " : "Ma'am ") + userdetails['firstname'] + " " + userdetails['lastname'] + `!</p>
                    <div style="
                    margin-top: 5px;
                    ">
                        <p style="
                        font-size:18px;
                        font-weight: 500;">A great day to learn and teach.</p>
                        <p style="
                        font-size:18px;
                        font-weight: 500;
                        position:relative;
                        top: -10px;">You have ` + numbers + ` class scheduled for today.</p>
                    </div>
                </div>
                <div style="
                margin-top: 10px; 
                margin-left: 40px; 
                ">            
                    <p style="
                    font-size: 18px;
                    font-weight: 600; 
                    "> ` + weatherNow['location']["localtime"] + ` </p>
                    <p>` + weatherNow["current"]["condition"]["text"] + ` <span style="display:inline-block;"><img style="width:25px; height:25px;" src="` + weatherNow["current"]["condition"]["icon"] + `"></img><span></p>
                </div>`;

            }

        const toRender = JSON.parse(message);
        const numbers = Object.keys(toRender).length;

        const schedules = compose_mini_schedule(toRender);

        document.querySelector('#schedule-today').innerHTML = schedules;
    }

    async function log_init(){
        document.querySelector('#write').style.opacity = "0";

        let {status, message} = await getData("GET", "/api/log/get-logs");
        if(status == "Error"){
            alert(message);
            location.reload();
        }
        if(status == "Empty"){
            return;
        }

        const logs = JSON.parse(message);

        let logContainer = ``;
        for(var i = 0; i < logs.length; i++){
            let container = `
                ` + JSON.stringify(logs[i]) + `
            `;
            logContainer += container;
        }

        document.querySelector('#table-container').innerHTML = logContainer;

    }

    async function admin_dashboard_init(){
        document.querySelector('#write').style.opacity = "0";

        const weatherNow = await $.ajax({
            type: "GET",
            url: "http://api.weatherapi.com/v1/current.json?key=090a4b6cd86a439bbe0145533232004&q=philippines&aqi=no",
            success: function(data){
                return data;
            }
        });

        //get log count
        let {status, message} = await getData("GET", "/api/log/count");

        if(status == "Error"){
            //throw error

            return;
        }

        document.querySelector('.greeting').innerHTML = `
        <div>
            <p style="
            font-size: 25px;
            font-weight: 700;
            min-width: 500px;
            max-width: 500px;
            ">Welcome, ` + (userdetails["sex"] == "Male" ? "Sir " : "Ma'am ") + userdetails['firstname'] + " " + userdetails['lastname'] + `!</p>
            <div style="
            margin-top: 5px;
            ">
                <p style="
                font-size:18px;
                font-weight: 500;">A greate day to take a rest.</p>
                <p style="
                font-size:18px;
                font-weight: 500;
                position:relative;
                top: -10px;">` + message + ` actvity logs made today.</p>
            </div>
        </div>
        <div style="
        margin-top: 10px; 
        margin-left: 40px; 
        ">            
            <p style="
            font-size: 18px;
            font-weight: 600; 
            "> ` + weatherNow['location']["localtime"] + ` </p>
            <p>` + weatherNow["current"]["condition"]["text"] + ` <span style="display:inline-block;"><img style="width:25px; height:25px;" src="` + weatherNow["current"]["condition"]["icon"] + `"></img><span></p>
        </div>`;

        ({status, message} = await getData("GET", "/api/log/get-recent"));

        if(status == "Error"){
            document.querySelector('#log-table').innerHTML = `<p>` + message + `</p>`;
            return;
        }

        //render the log table
        const logs = JSON.parse(message);
        
        let logContainer = ``;
        for(var i = 0; i < logs.length; i++){
            let container = `
                <tr>
                    <td>` + (i+1) + `</td>
                    <td>` + logs[i]['firstname'] + " " + logs[i]['middle'] + " " + logs[i]['lastname'] + `</td>
                    <td>` + logs[i]['role'] + `</td>
                    <td>` + logs[i]['status'] + `</td>
                    <td>` + logs[i]['type'] + `</td>
                </tr>
            `;

            logContainer += container;
        }

        document.querySelector('#log-table').innerHTML = logContainer;

    }

    async function instructors_init(){
        document.querySelector('#write').style.opacity = "0";

        let {status, message} = await getData("GET", "/api/account/get-all-instructors");

        if(status == "Empty"){
            document.querySelector('#empty').innerHTML = message;
        }
        if(status == "Error"){
            alert(message);
            location.reload();
        }

        const instructors = JSON.parse(message);

        let instContainer = ``;
        var c = 1;
        for(var i = 0; i < instructors.length; i++){
            var color = "";

            if(c == 1){
                color = "#0172B7";
                c++;
            } else {
                color = "#146C94";
                c++;
                c = 1;
            }
            let component = 
            `<div class="card" style="
            padding: 20px;
            background: ` + color + `;
            " data-schedule=''">
                <div style="
                    display: flex;
                ">
                    <div style="
                        height: 50px;
                        background-color: white;
                        text-align: center;
                        border-radius: 10px;
                    ">
                        <p style="
                            margin: 10px;
                            font-size: 18px;
                            font-weight: 700;
                            color: ` + color + `;
                        ">PIC</p>
                    </div>

                    <div>
                        <p style="                        
                            margin-left: 20px;
                            font-size: 18px;
                            font-weight: 900;
                        ">` + instructors[i]['firstname'] + " " + instructors[i]['lastname'] + `</p>
                        <p style="
                            margin-left: 20px;
                            font-size: 12px;
                            font-weight: 500;
                        ">MEMA</p>
                    </div>
                </div>

                <p style="
                    font-size: 14px;
                    margin-top: 5px;
                ">MEMA</p>
            </div>`;
            instContainer += component;
        }

        console.log(instContainer);

        document.querySelector('#instructor-container').innerHTML = instContainer;

    }



    async function classes_init(){
        document.querySelector('#write').setAttribute("onclick", "prepareClass()");
        document.querySelector('#write').style.opacity = "1";

        let {status, message} = await getData("GET", "/api/batch/get-batches");

        if(status == "Error"){
            document.querySelector('#notification-modal').innerHTML = `
                <h4>Error</h4>
                <p>` + message + `</p>
                <p onclick='hideNotification()'>Close</p>
            `;
            return;
        }

        const batches = JSON.parse(message);

        let content = ``;
        var c = 1;
        for(var i = 0; i < batches.length; i++){
            var color = "";

            if(c == 1){
                color = "#0172B7";
                c++;
            } else {
                color = "#146C94";
                c++;
                c = 1;
            }
            let component = 
            `<div class="card" style="
                padding: 20px;
                background: ` + color + `;
                box-shadow: 0 3px 5px 2px #c0c6ca;
            " data-schedule=''">
                <div style="
                    display: flex;
                ">
                    <div style="
                        height: 50px;
                        background-color: white;
                        text-align: center;
                        border-radius: 10px;
                    ">
                        <p style="
                            margin: 10px;
                            font-size: 18px;
                            font-weight: 700;
                            color: ` + color + `;
                        ">` + batches[i]['id'] +  `</p>
                    </div>

                    <div>
                        <p style="                        
                            margin-left: 20px;
                            font-size: 18px;
                            font-weight: 900;
                        ">` + batches[i]['course'] + " - " + batches[i]['section'] + `</p>
                        <p style="
                            margin-left: 20px;
                            font-size: 12px;
                            font-weight: 500;
                        ">` + 0 + ` students</p>
                    </div>
                </div>

                <p style="
                    font-size: 14px;
                    margin-top: 10px;
                    padding-left: calc(100% - 30px);
                ">
                    <span><img onclick="prepareEditClass(` + batches[i]['id'] + `)" style="margin-left: 5px; cursor: pointer; width: 25px; height: 25px; filter: invert(1)" src="/scripts/edit.svg"></span>
                <p>
            </div>`;
            content += component;
        }

        document.querySelector('#class-container').innerHTML = content;
    }

    async function schedule_init(){

        let {status, message} = await getData("GET", "/api/account/" + userdetails['id'] + "/schedules")
        console.log(status + " " + message);

        if(status == "Error"){
            
        }

        if(status == "Empty"){
            //display
            document.querySelector('#empty').innerHTML = message;
        }

        cardsData = JSON.parse(message);

        var content = ""; 
        var c = 1;
        for(let i = 0; i < objects.length; i++){
            var color = "";

            if(c == 1){
                color = "#0172B7";
                c++;
            } else {
                color = "#146C94";
                c++;
                c = 1;
            }
 
            var day = objects[i]['weekslot'].substring(0, 3);
            day = day.toUpperCase();

            let component = 
            `<div class="card" style="
            padding: 20px;
            background: ` + color + `;
            " data-schedule='` + JSON.stringify(objects[i]) + `' onclick="renderStudents(this)">
                <div style="
                    display: flex;
                ">
                    <div style="
                        height: 50px;
                        background-color: white;
                        text-align: center;
                        border-radius: 10px;
                    ">
                        <p style="
                            margin: 10px;
                            font-size: 18px;
                            font-weight: 700;
                            color: ` + color + `;
                        ">` + day + `</p>
                    </div>

                    <div>
                        <p style="                        
                            margin-left: 20px;
                            font-size: 18px;
                            font-weight: 900;
                        ">` + objects[i]['batch']['course'] + " - " + objects[i]['batch']['section'] + `</p>
                        <p style="
                            margin-left: 20px;
                            font-size: 12px;
                            font-weight: 500;
                        ">` + objects[i]['start_at'] + " - " + objects[i]['end_at'] + `</p>
                    </div>
                </div>

                <p style="
                    font-size: 14px;
                    margin-top: 5px;
                ">` + objects[i]['subjectcode'] + " - " + objects[i]['subjectname'] +`</p>
            </div>`;

            content += component;
        }

        document.querySelector('.cards').innerHTML = content;
    }

    var typingTimer;                //timer identifier
    var doneTypingInterval = 1500;  //time in ms, 5 seconds for example
    
    //on keyup, start the countdown
    function check(){
        clearTimeout(typingTimer);
        if ($('#search').val()) {
            typingTimer = setTimeout(search, doneTypingInterval);
        }
    }
    
    //user is "finished typing," do something
    async function search() {
      //do something
        var filter = document.querySelector("#filter-box");
        var constraint = filter.options[filter.selectedIndex].value;
        const search = document.querySelector('#search').value;

        let query = {
            search,
            constraint,
            filter: "ASC"
        }

        let {status, message} = await sendData("/api/search/student", query);
        if(status == "Error"){
            return;
        }
        if(status == "Empty"){
            return;
        }

        const students = JSON.parse(message);

        let content = ``;
        for(var i = 0; i < students.length; i++){
            let container = `
                <tr>
                    <td>` + (i+1) + `</td>
                    <td>` + students[i]['firstname'] + " " + students[i]['middlename'] + " " + students[i]['lastname'] + `</td>
                    <td>` + students[i]['batchId']['course'] + " - " + students[i]['batchId']['section'] + `</td>
                    <td>` + students[i]['studentid'] + `</td>
                    <td>` + students[i]['address'] + `</td>
                    <td>
                        <span style="cursor: pointer;" onclick="prepareEditStudent(` + students[i]['id'] + `)">Edit</span>
                        <span>Remove</span>
                    </td>
                </tr>
            `;
            content += container;
        }

        document.querySelector('#table-container').innerHTML = content;

    }

    async function student_init(){
        document.querySelector('#write').style.opacity = "1";
        document.querySelector('#write').setAttribute('onclick', "prepareStudentForm()");

        let {status, message} = await getData("GET", "/api/student/get-students");

        if(status == "Empty"){
            //throw empty
        }
        if(status == "Error"){
            //throw error
        }

        const students = JSON.parse(message);
        console.log(students);
        let stdContainer = ``;
        for(var i = 0; i < students.length; i++){
            let content = `
                <tr>
                    <td>` + (i+1) + `</td>
                    <td>` + students[i]['firstname'] + " " + students[i]['middlename'] + " " + students[i]['lastname'] + `</td>
                    <td>` + students[i]['batchId']['course'] + " - " + students[i]['batchId']['section'] + `</td>
                    <td>` + students[i]['studentid'] + `</td>
                    <td>` + students[i]['address'] + `</td>
                    <td>
                        <span style="cursor: pointer;" onclick="prepareEditStudent(` + students[i]['id'] + `)">Edit</span>
                        <span>Remove</span>
                    </td>
                </tr>
            `;
            stdContainer += content;
        }

        document.querySelector('#table-container').innerHTML = stdContainer;
    }

    async function nav_init(){
        let list = document.querySelectorAll('.list');
        for(let i = 0; i < list.length; i++){
            list[i].addEventListener('click', async function(){
                console.log("clicked");
                let x = 0;
                while (x < list.length){
                    list[x++].className = 'list';
                }
                list[i].className = 'list active';
                await compose_content(i);
            });
        }
    }

