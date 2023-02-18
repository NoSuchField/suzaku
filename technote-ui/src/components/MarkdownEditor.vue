<template>
    <!-- :indent-with-tab="true" 是否自动获取焦点-->
    <codemirror v-model="code" placeholder="Code gose here..." :style="{ height: 'calc(100% - 150px)' }" :autofocus="true"
        :tabSize="2" :extensions="extensions" />
</template>
<!-- @ready="ready('ready', $event)"
        @change="Change('change', $event)" @focus="focus('focus', $event)" @blur="blur('blur', $event)" -->
<script lang="ts" setup>
import { Codemirror } from "vue-codemirror";
import { markdown } from "@codemirror/lang-markdown";
import { oneDark } from "@codemirror/theme-one-dark";
import { ref } from "vue";
import { EditorView } from "@codemirror/view"

let myTheme = EditorView.theme({
    // 输入的字体颜色
    "&": {
        color: "#0052D9",
        backgroundColor: "#252526"
    },
    ".cm-content": {
        caretColor: "#0052D9",
    },
    // 激活背景色
    ".cm-activeLine": {
        backgroundColor: "#FAFAFA"
    },
    // 激活序列的背景色
    ".cm-activeLineGutter": {
        backgroundColor: "#FAFAFA"
    },
    //光标的颜色
    "&.cm-focused .cm-cursor": {
        borderLeftColor: "#0052D9"
    },
    // 选中的状态
    "&.cm-focused .cm-selectionBackground, ::selection": {
        backgroundColor: "#0052D9",
        color:'#252526'
    },
    // 左侧侧边栏的颜色
    ".cm-gutters": {
        backgroundColor: "#252526",
        color: "#ddd", //侧边栏文字颜色
        border: "none"
    }
}, { dark: true })
interface IProps {
    height?: string,
}
// 接受的参数
const props = withDefaults(defineProps<IProps>(), {
    height: '400px'
})
const code = ref(``);
const extensions = [markdown(), myTheme];
const Change = () => {

}



</script>

<style>
.cm-content {
    overflow-y: scroll;
}
</style>