<template>
    <div>
        <Modal class-name="vertical-center-modal" v-model="modal.show" title="注册接口" width="800">
            <div>
                <Form v-model="form" :label-width="80">
                    <Row>
                        <Col :span="6">
                            <Select prefix="md-planet" style="" v-model="form.protocol">
                                <Option v-for="(item, i) in protocolOptions" :key="i" :value="item.value">
                                    {{item.label}}
                                </Option>
                            </Select>
                        </Col>
                        <Col :span="18">
                            <Input v-model="form.name" placeholder="输入接口名称"></Input>
                        </Col>
                    </Row>
                    <Row>
                        <Col :span="6">
                            <Select prefix="md-megaphone" v-model="form.httpMethod">
                                <Option v-for="(item, i) in httpMethodOptions" :key="i" :value="item.value">
                                    {{item.value}}
                                </Option>
                            </Select>
                        </Col>
                        <Col :span="12">
                            <Input v-model="form.url" placeholder="输入请求地址"></Input>
                        </Col>
                        <Col :span="6">
                            <Select prefix="md-return-right" v-model="form.returnType">
                                <Option v-for="(type, i) in typeOptions" :key="i" :value="type.value">
                                    {{type.label}}
                                </Option>
                            </Select>
                        </Col>
                    </Row>
                    <div>
                        <Row v-for="(param, index) in form.params" :key="index">

                            <Col :span="2">
                                <Input v-model="param.index" disabled></Input>
                            </Col>
                            <Col :span="10">
                                <Select prefix="md-return-right" v-model="param.type" placeholder="选择参数类型">
                                    <Option v-for="(type, i) in typeOptions" :key="i" :value="type.value">
                                        {{type.label}}
                                    </Option>
                                </Select>
                            </Col>
                            <Col :span="10">
                                <Input v-model="param.name" placeholder="输入参数名称"></Input>
                            </Col>
                            <Col :span="2">
                                <i class="op-icon el-icon-delete" style="color: orangered" @click="Param.del(param)"></i>
                                <i class="op-icon el-icon-sort-down" style="color: #367d92;" @click="Param.down(param)"></i>
                                <i class="op-icon el-icon-sort-up" style="color: #367d92;" @click="Param.up(param)"></i>
                            </Col>
                        </Row>
                    </div>
                    <Row>
                        <Col :span="24">
                            <codemirror
                                ref="myCm"
                                :value="form.code"
                                :options="cmOptions"
                                @changes="onCodeChange"
                                class="code">
                            </codemirror>
                        </Col>
                    </Row>
                </Form>
            </div>
            <template #footer>
                <Button type="primary" @click="Param.push">添加参数</Button>
                <Button type="success" @click="register">确认安装</Button>
            </template>
        </Modal>
    </div>
</template>

<script>
    import {registerApi} from "@/api/api";
    import { codemirror } from 'vue-codemirror'
    import 'codemirror/lib/codemirror.css'
    import "codemirror/theme/darcula.css";
    require("codemirror/mode/clike/clike.js");
	export default {
        name: "api-add",
        components:{
            codemirror
        },
        props: {
            modal: {
                show: Boolean,
                afterFinish: Function,
                metadata: Object,
            }
        },
        data() {
            return {
                protocolOptions: [
                    {
                        label: "HTTP",
                        value: 1,
                    }
                ],

                typeOptions: [
                    {
                        label: "Void",
                        value: "java.lang.Void",
                    },
                    {
                        label: "Object",
                        value: "java.lang.Object",
                    },
                    {
                        label: "Boolean",
                        value: "java.lang.Boolean",
                    },
                    {
                        label: "String",
                        value: "java.lang.String",
                    },
                    {
                        label: "Integer",
                        value: "java.lang.Integer",
                    },
                    {
                        label: "Double",
                        value: "java.lang.Double"
                    },
                    {
                        label: "Float",
                        value: "java.lang.Float",
                    },
                ],
                httpMethodOptions: [
                    {
                        value: "GET",
                    },
                    {
                        value: "HEAD",
                    },
                    {
                        value: "POST",
                    },
                    {
                        value: "PUT"
                    },
                    {
                        value: "PATCH",
                    },
                    {
                        value: "DELETE",
                    },
                    {
                        value: "OPTIONS",
                    },
                    {
                        value: "TRACE",
                    },
                ],

                form: {
                    protocol: 1,
                    name: null,
                    httpMethod: 'GET',
                    url: null,
                    code: '// 欸嘿嘿\n',
                    returnType: '',
                    params: [],
                },
                cmOptions:{
                    value: '',
                    mode: "text/x-java",
                    theme: "darcula",
                    height: 500,
                    autofocus: true,
                    lineNumbers: true,          // 显示行号
                    smartIndent: true,          // 自动缩进
                    autoCloseBrackets: true,    // 自动补全括号
                    styleActiveLine: true,      // 高亮选中行
                    autofocus: true,
                    hintOptions: {
                        completeSingle: true    // 当匹配只有一项的时候是否自动补全
                    }
                },
                Param: {
                    // 添加参数
                    push: () => {
                        this.form.params.push(
                            {
                                type: null,
                                name: null,
                                index: this.form.params.length + 1,
                            }
                        );
                        this.Param.sort();
                    },

                    sort: () => {
                        this.form.params.sort((x, y) => x.index - y.index);
                    },

                    del: param => {
                        this.form.params.splice(param.index -1, 1);
                        if (this.form.params.length > 0) {
                            this.form.params.forEach((p, i) => p.index = i + 1);
                        }
                    },

                    up: param => {
                        let i = param.index - 1;
                        if (this.form.params[i - 1]) {
                            this.form.params[i - 1].index++;
                            this.form.params[i].index--;
                        }
                        this.Param.sort();
                    },
                    down: param => {
                        let i = param.index - 1;
                        if (this.form.params[i + 1]) {
                            this.form.params[i + 1].index--;
                            this.form.params[i].index++;
                        }
                        this.Param.sort();
                    },
                }
            }
        },
        mounted() {
            this.$nextTick( () =>{
                this.codemirror.refresh()
            });
            if (this.modal.metadata) {
                this.form = this.modal.metadata;
            }
        },
        computed: {
            codemirror() {
                return this.$refs.myCm.codemirror
            }
        },
        methods: {
            register() {
                registerApi(this.form).then(res => {
                    if (this.modal.afterFinish) {
                        this.modal.afterFinish();
                    }
                    this.modal.show = false;
                }).catch(err => {
                    console.log(err)
                })
            },
            onCodeChange() {
                this.form.code = this.codemirror.getValue();
            },
        }
	}
</script>

<style>
    .CodeMirror {
        border: 1px solid #eee;
        height: 500px !important;
    }
</style>

<style lang="less">

    .vertical-center-modal {
        display: flex;
        align-items: center;
        justify-content: center;
        .ivu-modal {
            top: 0;
        }
    }
</style>
<style scoped>
    .op-icon {
        cursor: pointer;
        margin-top: 8px;
        font-size: 18px;
    }
</style>
