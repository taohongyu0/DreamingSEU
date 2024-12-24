document.addEventListener('DOMContentLoaded', function() {
    let imageUrl = '../pictures/userProfiles/default.png';
    const profilePicture = document.getElementById('profilePicture');
    let username = document.getElementById('username');
    let name = document.getElementById('name');
    let reputation = document.getElementById('reputation');
    let hits = document.getElementById('hits');
    let loginTime = document.getElementById('loginTime');
    let trueUsername;//数据库中传回来的用户名
    profilePicture.src = imageUrl;
    fetch("http://localhost:8088/user/personalCenterInfo",{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body : localStorage.getItem("token")
        })
        .then(response => {
            // 检查响应是否成功（状态码200-299）
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            // 返回JSON格式的数据（假设后台返回的是JSON）
            return response.json();
        })
        .then(data => {
            if(data.profileExist==true){
                // 假设返回的JSON对象中有一个'imageUrl'字段包含了图片的URL
                imageUrl = "../pictures/userProfiles/"+data.id+".png";
                // 设置<img>元素src属性
                profilePicture.src = imageUrl;
            }
            trueUsername=data.username;
            document.getElementById('username').innerText = "@"+data.username;
            document.getElementById('name').innerText = data.name;
            document.getElementById('reputation').innerText = data.reputation;
            document.getElementById('hits').innerText = data.totalHits;
            document.getElementById('loginTime').innerText = data.loginTimeChinese;
        })
        .catch(error => {
            // 处理错误（例如，显示错误消息）
            console.error('There was a problem with the fetch operation:', error);
        });


    // 获取所有链接元素
    var viewMyBlog = document.getElementById('viewMyBlog');
    var changeInfo = document.getElementById('changeInfo');
    var changeProfile = document.getElementById('changeProfile');
    var changePassword = document.getElementById('changePassword');

    // 为每个链接添加点击事件监听器
    viewMyBlog.addEventListener('click', function(event) {
        event.preventDefault(); // 阻止默认行为（即不跳转至 href 指定的 URL）
        window.location.href = '../webpage/myBlog.html'; // 跳转到指定页面
    });

    changeInfo.addEventListener('click', function(event) {
        event.preventDefault(); // 阻止默认行为（即不跳转至 href 指定的 URL）
        const data = { username: trueUsername }; // 需要传递的数据
        const queryString = new URLSearchParams(data).toString(); // 转换为查询字符串
        window.location.href = `../webpage/changeInfo.html?${queryString}`; // 跳转到指定页面
    });

    changePassword.addEventListener('click', function(event) {
        event.preventDefault(); // 阻止默认行为（即不跳转至 href 指定的 URL）
        const data = { username: trueUsername }; // 需要传递的数据
        const queryString = new URLSearchParams(data).toString(); // 转换为查询字符串
        window.location.href = `../webpage/changePassword.html?${queryString}`; // 跳转到目标页面并附带查询参数
    });

    changeProfile.addEventListener('click', async function (event) {
        event.preventDefault();
        const fileInput = document.getElementById('fileInput');
        fileInput.click();
        // let userId;
        // //先获取userId
        // fetch('http://localhost:8088/token/getUserId',{
        //     method: 'POST',
        //     headers: {
        //         'Content-Type': 'application/json'
        //     },
        //     body : localStorage.getItem("token")
        // })
        //     .then(response => {
        //         if (!response.ok) {
        //             alert("response-网络响应失败");
        //             throw new Error('网络响应失败');
        //         }
        //         return response.json();
        //     })
        //     .then(data => {
        //         userId = data.data;
        //         fileInput.click();
        //     })
        //     .catch(error => {
        //         alert("error-请求失败")
        //         console.error('请求失败:', error);
        //     });



        fileInput.addEventListener('change', async function (event) {
            if (!fileInput.files.length) {
                alert('Please select an image file first.');
                return;
            }
            const formData = new FormData();
            formData.append('file', fileInput.files[0]);
            formData.append('token',localStorage.getItem('token'));
            try {
                const response = await fetch('http://localhost:8088/user/uploadProfile', {
                    method: 'POST',
                    body: formData
                });
                if (!response.ok) {
                    throw new Error('Failed to upload image: ' + response.statusText);
                }

                // const result = await response.json(); // 假设服务器返回JSON格式的响应
                // const imageUrl = result.imageUrl; // 从响应中提取图片的URL
                //
                // if (imageUrl) {
                //     displayImage.src = imageUrl;
                //     displayImage.style.display = 'block'; // 显示图片
                // } else {
                //     alert('No image URL returned from the server.');
                // }
            } catch (error) {
                alert('Error: ' + error.message);
            }

            window.location.href = 'personalCenter.html';
        });
    });
});