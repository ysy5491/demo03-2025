<script lang="ts">
    import { form } from "$app/server";

    // 할 일 추가 폼 컴포넌트
    type Todo = {
        id: number;
        body: string;
        done: boolean;
        edting: boolean;
    };

    let todosLastId = 0;
    const todos = $state<Todo[]>([]);
    const todosCount = $derived(todos.length);
    const doneCount = $derived(todos.filter(todo => todo.done).length);
    const donePercent = $derived(todos.length === 0 ? 0 : Math.round((doneCount / todosCount) * 100));

    function addTodo(this: HTMLFormElement) {
        const form: HTMLFormElement = this;


        form.body.value = form.body.value.trim();

        if (form.body.value.length === 0) {
            alert("할 일을 입력해주세요.");
            form.body.focus();
            return;
        }

        let body = $state(form.body.value);
        let done = $state(false);
        let edting = $state(false);

        todos.push({
            id: ++todosLastId, 
            get body() {
                return body;
            },
            set body(value: string) {
                body = value;
            },
            get done() {
                return done;
            },
            set done(value: boolean) {
                done = value;
            },
            get edting() {
                return edting;
            },
            set edting(value: boolean) {
                edting = value;
            }
        });
    }

    function deleteTodo(todoToDelete: Todo) {
        const index = todos.findIndex(todo => todo.id === todoToDelete.id);
        if (index !== -1) {
            todos.splice(index, 1);
        }
    }

    $effect(() => {
        console.log("현재 할 일 리스트:", todos);
    });

    function modifyTodo(form: HTMLFormElement, todo: Todo) {
        form.body.value = form.body.value.trim();

        form.body.value = form.body.value.trim();

        if (form.body.value.length === 0) {
            alert("할 일을 입력해주세요.");
            form.body.focus();
            return;
        }

        todo.body = form.body.value;
        todo.edting = false;
    }
</script>

<h1>할 일 앱</h1>

<h2>할 일 추가</h2>
<form onsubmit={addTodo}>
    <input type="text" name="body" placeholder="할 일 입력"/>
    <button type="submit">추가</button>
</form>

<h2>할 일 리스트({doneCount}/{todosCount})</h2>
<p>진행률: {donePercent}%</p>
<ul>
    {#each todos as todo (todo.id)}
        <li style="display: flex; align-items: center; gap: 8px; margin-bottom: 8px;">
            <input type="checkbox" bind:checked={todo.done}/>
            <span>
                {todo.id}
            </span><span>&nbsp;: &nbsp;</span><span>
            </span>

            {#if todo.edting}
                <form style="display: flex; align-items: center; gap: 8px; margin-bottom: 8px;" onsubmit={(event) => modifyTodo(event.target as HTMLFormElement, todo)}>
                    <input type="text" name="body" placeholder="할 일 입력" bind:value="{todo.body}"/>
                    <button type="submit">적용</button>
                    <button type="button" onclick={() => todo.edting = false}>수정취소</button>
                </form>
            {:else}
                {todo.body} {todo.done ? "(완료)" : ""}
                <input type="button" value="삭제" onclick={event => { event.preventDefault(); deleteTodo(todo); }}/>
                <button type="button" onclick={() => todo.edting = true}>수정</button>
            {/if}
        </li>
    {/each}
</ul>   

<style>
    h1 {
        color: #333;
    }

    h2 {
        color: #555;
    }

    form {
        margin-bottom: 20px;
    }

    input[type="text"] {
        padding: 8px;
        width: 200px;
        margin-right: 10px;
    }

    button {
        padding: 8px 12px;
        background-color: #28a745;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    button:hover {
        background-color: #218838;
    }

    ul {
        list-style-type: none;
        padding: 0;
    }

    li {
        margin-bottom: 10px;
    }

    input[type="button"] {
        margin-left: 10px;
        padding: 4px 8px;
        background-color: #dc3545;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    input[type="button"]:hover {
        background-color: #c82333;
    }

    body {
        font-family: Arial, sans-serif;
        background-color: #f0f0f0;
        padding: 20px;
    }

    p {
        font-size: 1rem;
        color: #666;
        margin-bottom: 20px;
    }

</style>