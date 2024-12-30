document.addEventListener('DOMContentLoaded', () => {
    const postList = document.getElementById('post-list');
    apiUrl = 'http://localhost:8088/article/blogPush';
    userToken = localStorage.getItem("token");

    // Fetch API 请求后端数据
    fetch(apiUrl,{
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: userToken
    })
        .then(response => {
            if (!response.ok) {
                alert("response-网络响应失败");
                throw new Error('网络响应失败');
            }
            return response.json();
        })
        .then(data => {
            data.forEach(post => {
                createPostItem(post);
            });
        })
        .catch(error => {
            alert("error-请求失败")
            console.error('请求失败:', error);
        });

    function createPostItem(post) {
        const postItem = document.createElement('div');
        postItem.className = 'post-item';
        const postTextItem = document.createElement('div');
        postTextItem.className = 'post-text-item';

        const postTitle = document.createElement('a');
        postTitle.className = 'post-title';
        //postTitle.href = `/post/${post.id}`;
        // 为链接添加点击事件监听器
        postTitle.addEventListener('click', (event) => {
            // 阻止链接的默认跳转行为（如果需要的话）
            // event.preventDefault();

            // 在localStorage中保存文章ID（或其他您想要保存的数据）
            localStorage.setItem('tempArticleId', post.id);

            // 如果您希望继续执行链接的默认跳转行为，请不要注释掉下一行代码
            // window.location.href = postTitle.href; // 或者使用 event.currentTarget.href，它们在此情境下是等价的
        });
        postTitle.href = 'articleView.html'; // 假设点击后跳转到具体文章页面
        postTitle.textContent = post.title;

        // 创建封禁账号按钮
        const banButton = document.createElement('button');
        banButton.className = 'delete-button'; // 可以为删除按钮添加一个特定的类名用于样式和脚本
        if(post.allowComment===true){
            banButton.textContent = '评论区已开启';
            banButton.classList.remove('user-unban-set');
            banButton.classList.add('user-ban-set');
        }
        else{
            banButton.textContent = '评论区已关闭';
            banButton.classList.remove('user-ban-set');
            banButton.classList.add('user-unban-set');
        }


        // 为封禁按钮添加点击事件监听器
        banButton.addEventListener('click', (event) => {
            const banUrl = 'http://localhost:8088/admin/banArticleComment';

            fetch(banUrl,{
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: post.id
            })
                .then(response => {
                    if (!response.ok) {
                        alert("封禁失败");
                        throw new Error('封禁失败');
                    }
                    return response.json();
                })
                .then(data=>{
                    if(data.data==="成功打开评论区"){
                        banButton.textContent = '评论区已开启';
                        banButton.classList.remove('user-unban-set');
                        banButton.classList.add('user-ban-set');
                    }
                    else if(data.data==="成功关闭评论区"){
                        banButton.textContent = '评论区已关闭';
                        banButton.classList.remove('user-ban-set');
                        banButton.classList.add('user-unban-set');
                    }
                })
                .catch(error => {
                    alert("封禁失败")
                    console.error('封禁失败：', error);
                });
            // 注意：这里没有阻止事件的默认行为，因为按钮的默认行为就是触发点击事件
        });

        // 创建删除按钮
        const deleteButton = document.createElement('button');
        deleteButton.className = 'delete-button'; // 可以为删除按钮添加一个特定的类名用于样式和脚本
        deleteButton.textContent = '删除';

        // 为删除按钮添加点击事件监听器
        deleteButton.addEventListener('click', (event) => {
            const userConfirmed = confirm('确认删除该博客?');
            if(userConfirmed){
                // 从 DOM 中移除 postItem
                postItem.parentNode.removeChild(postItem);
                const deleteUrl = 'http://localhost:8088/article/deleteArticle';

                fetch(deleteUrl,{
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: post.id
                })
                    .then(response => {
                        if (!response.ok) {
                            alert("删除失败");
                            throw new Error('删除失败');
                        }
                        return response.json();
                    })
                    .catch(error => {
                        alert("删除失败")
                        console.error('删除失败：', error);
                    });
            }
        });

        const postSummary = document.createElement('p');
        postSummary.className = 'post-summary';
        postSummary.textContent = "作者："+post.authorName;

        postTextItem.appendChild(postTitle);
        postTextItem.appendChild(postSummary);
        postItem.appendChild(postTextItem);
        postItem.appendChild(banButton);
        postItem.appendChild(deleteButton); // 将删除按钮添加到 postItem 的末尾

        postList.appendChild(postItem);
    }
});

document.addEventListener('DOMContentLoaded', () => {
    let clearPicButton = document.getElementById("clearPic");
   clearPicButton.addEventListener('click',()=>{
       const clearPicUrl = "http://localhost:8088/admin/clearPic";
       fetch(clearPicUrl,{
           method: 'POST',
           headers: {
               'Content-Type': 'application/json'
           }
       })
           .then(response => {
           if (!response.ok) {
               alert("清除失败");
               throw new Error('清除失败');
           }
           else{
               alert("清除成功");
           }
           return response.json();
       })
           .catch(error => {
               alert("清除失败")
               console.error('清除失败：', error);
           });
    });
});