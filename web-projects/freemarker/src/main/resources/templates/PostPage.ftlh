<#import "commons.ftlh" as c>

<@c.page>
    <@c.post post=post/>
    <br>
    <#if user??>
        <div class="form">
            <div class="body">
                <form method="post" action="/post/${post.id}/add-comment">
                    <div class="field">
                        <div class="name">
                            <label for="text">Add your comment:</label>
                        </div>
                        <div class="value">
                            <textarea style="resize: none" id="text" name="text"></textarea>
                        </div>
                        <@c.error "post.text"/>
                    </div>
                    <div class="button-field">
                        <input type="submit" value="Add">
                    </div>
                </form>
            </div>
        </div>
    </#if>
    <br>
    <h4>Other comments:</h4>
    <#if comments?size == 0>
        <p>There are no comments under this post. But you can leave the first one right now</p>
    <#else>
        <#list comments as comment>
            <div class="message">${comment.text}</div>
        </#list>
    </#if>
</@c.page>
