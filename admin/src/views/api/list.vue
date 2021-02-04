<template>
    <div class="app-container">
        <Row>
            <Button class="btn-header" type="primary" @click="getList" icon="md-refresh">刷新</Button>
            <Button class="btn-header" type="success" @click="showAdd" icon="md-add">添加</Button>
        </Row>
        <Table border :columns="columns" :data="list">
            <template slot-scope="{row}" slot="action">
                <Button size="small" type="primary" ghost @click="edit(row)">编辑</Button>
            </template>
        </Table>
        <api-add :modal="modal" v-if="modal.show"></api-add>
    </div>
</template>

<script>
    import {getApiList, getApiDetail} from "@/api/api";
    import ApiAdd from "@/views/api/api-add";

    export default {
		name: "list",
        components: {ApiAdd},
        data() {
		    return {
		        list: [],
		        columns: [
                    {
                        title: "接口名称",
                        key: "name",
                        align: "center",
                        width: 120,
                    },
                    {
                        title: "HTTP",
                        key: "httpMethod",
                        align: "center",
                        width: 120,
                    },
                    {
                        title: "URI",
                        key: "url",
                        align: "center",
                        width: 120,
                    },
                    {
                        title: "method",
                        key: "method",
                        align: "center",
                    },
                    {
                        title: "操作",
                        slot: "action",
                        align: "center",
                        width: 120,
                    },
                ],
                modal: {
		            show: false,
                    afterFinish: this.getList,
                    metadata: null,
                }
            }
        },
        mounted() {
		    this.getList();
        },
        methods: {
            edit(api) {
                getApiDetail(api.name).then(api => {
                    this.modal.metadata = api;
                    this.modal.show = true;
                }).catch(err => {
                    console.log(err);
                })
            },
		    getList() {
		        getApiList().then(list => {
		            this.list = list;
                }).catch(err => {
                    console.log(err)
                })
            },
            showAdd() {
                this.modal.metadata = null;
		        this.modal.show = true;
            }
        }
	}
</script>

<style scoped>
    .btn-header {
        margin: 10px 5px;
    }
</style>
