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

    // eraseCookie('state');
    // eraseCookie('user');

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

        draw_curtain();

    })();

    async function logout(){
        await eraseCookie('state');
        await eraseCookie('user');
        location.reload();
    }

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

    // async function openNav() {
    //     var form = new FormData(document.querySelector('#reg-form'));

    //     const fullname = form.get('fullname');
    //     const password = form.get('password');
    //     const repass = form.get('repass');

    //     // some inputs are empty
    //     if(username === "" ||
    //     fullname === "" ||
    //     password === "" ||
    //     repass == ""){
    //         document.querySelector('#error-placer').innerHTML = "Please fill in the form."; //send warning
    //         return;
    //     }

    //     let account = {
    //         username,
    //         fullname,
    //         password,
    //         sex,
    //         role
    //     };

    //     console.log(JSON.stringify(account));
    //     let { status, message } = await sendData("/api/account/create-account", account);

    //     if(status == "Used"){
    //         //username is already in used
    //         document.querySelector('#error-placer').innerHTML = message;
    //         return;
    //     }
    //     if(status == "Wait"){
    //         // wait for the account confirmation
    //         // route to login
    //         return;
    //     }

    //     //route to dashboard

    //     return;
    //     setTimeout(function (){
    //         const sb = document.querySelector('.sexBox');
    //         sb.className = "sexBox now-active";
    //         $('.curtain').append("<img src='https://i0.wp.com/neust.edu.ph/wp-content/uploads/2020/06/cropped-neust_logo-1-1.png?w=512&ssl=1' class='top-logo' alt='' style='animation: fade 3s; position: absolute; top: 30px; left: 30px; width: 80px; height: 80px;'>");
    //     }, 3000);

    //     var options = document.querySelectorAll('.sex');
    //     options.forEach(option => {
    //         option.addEventListener('click', function(){
    //             next(formData, this.dataset.value);
    //         });
    //     });
    // }

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

    // server call
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

    // clicked the register now function
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

    //click the login now function
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

    // login button function
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

    async function setUser(){
        document.querySelector('#user-name').innerHTML = userdetails['firstname'] + " " + userdetails['lastname'];
        document.querySelector('#role').innerHTML = userdetails['role'];
    }

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

    // mainscreen configuration
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

        let batchContent = ``;

        if(status == "Empty"){
            batchContent = message;
        } else {
            const batches = JSON.parse(message);

            for(var i = 0; i < batches.length; i++){
                let container = `
                    <option value=` + batches[i]['id'] + `>` + batches[i]['course'] + " - " + batches[i]['section'] + `</option>
                `;
    
                batchContent += container;
            }
        }

        document.querySelector('#batch').innerHTML = batchContent;
    }

    function refresh(){
        location.reload();
    }

    //create schedule
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

    async function compose_content(id){
        var userCookie = getCookie('user');
        const user = JSON.parse(userCookie);
        const role = user['role'];

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
                    setCookie('state','Logs', 1);
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
                    setCookie('state','Instructors', 1);
                    setCookie('user', JSON.stringify(userdetails), 1);
            }
        }
    }

    async function log_init(){
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
        let {status, message} = await getData("GET", "/api/account/get-all-instructors");

        if(status == "Error"){
            document.querySelector('#empty').innerHTML = message;
        }

        const instructors = JSON.parse(message);

        let instContainer = ``;
        for(var i = 0; i < instructors.length; i++){

            let container = `
                ` + JSON.stringify(instructors[i]) + `
            `;
            instContainer += container;
        }

        console.log(instContainer);

        document.querySelector('#instructor-container').innerHTML = instContainer;



    }

    async function classes_init(){
        
        const container = document.querySelector(".cards");
            const boxs = document.querySelectorAll(".card");

            boxs.forEach(box => {
                box.addEventListener("click", function() {
                    boxs.forEach(b => {
                    if(b !== box) {
                        b.style.display = "none";
                    }
                }); 
                box.classList.add('grow');
                box.style.backgroundColor="white";
            });
        });

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
        let cards = await compose_card(cardsData);

        document.querySelector('.cards').innerHTML = cards;
        
        // const logo = document.querySelector(".cards");
        //     const boxs = document.querySelectorAll(".card");

        //     boxs.forEach(box => {
        //         box.addEventListener("click", function() {
        //             boxs.forEach(b => {
        //                 if(b !== box) {
        //                     b.style.display = "none";
        //                 }
        //             }); 
        //             box.classList.add('grow');
        //             box.style.backgroundColor="white";
        //         });
        //     });

        // $('#exampleModal').on('show.bs.modal', function (event) {
        //     var button = $(event.relatedTarget)
        //     var recipient = button.data('whatever')
        //     var modal = $(this)
        //     modal.find('.modal-title').text('New schedule ' + recipient)
        //     modal.find('.modal-body input').val(recipient)
        // })

        // const btn_submit = document.querySelector(".btn-submit");

        // btn_submit.addEventListener('click', e =>{
        //     const modal = document.querySelector(".modal");
        //     const modalOpen = document.querySelector(".modal-open");
        //     const modalback = document.querySelector(".modal-backdrop");

        //     const instructor = document.querySelector(".instructor").value;
        //     const subject = document.querySelector(".subject").value;
        //     const time = document.querySelector(".time").value;
        //     const val = document.querySelector(".value").value;


        //     console.log("-------------");

        //     console.log(val);
        //     console.log(instructor);
        //     console.log(subject);
        //     console.log(time);

        //     modal.style.display = "none";
        //     modal.classList.remove("show");
        //     modalback.remove();
        //     modalOpen.classList.remove("modal-open");

        //     const modalElement = document.querySelector('[aria-modal="true"]');

        //     modalElement.removeAttribute('aria-modal');
        //     modalElement.setAttribute('aria-hidden', 'true');
        // })

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

// 
// 
// 
// 
//     

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

    async function compose_card(objects){
        var constructed = ""; 
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

            constructed += component;
        }

        return constructed;
    }

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

    function compose_mini_schedule(schedules, limit){
        let container = "<tr></tr>";
        schedules.forEach((schedule)=>{
            const content = `            
            <tr>
                <td class="table-item" style="min-width: 200px; max-width: 200px; margin-right:10px;">` + schedule["subjectcode"] + ` - ` + schedule["timeslot"] + `</td>
                <td class="" style="margin-left: 70px; min-width:80px; max-width: 80px;">` + schedule["batch"]["course"] + " - " + schedule["batch"]["section"] + `</td>
            </tr>`;

            container += content;
        });

        return container;
    }

    function showAddScheduleModal(){
        $('#scheduleModal').modal('show');
    }

