
const buff = {
    Sakyamuni:
        '/*' +
        '   芜湖~起飞！\n' +
        '   \n' +
        '\n' +
        '                    _ooOoo_\n' +
        '                   o8888888o\n' +
        '                   88" . "88\n' +
        '                   (| -_- |)\n' +
        '                   O\\  =  /O\n' +
        '                ____/`---\'\\____\n' +
        '              .\'  \\\\|     |//  `.\n' +
        '             /  \\\\|||  :  |||//  \\\n' +
        '            /  _||||| -:- |||||-  \\\n' +
        '            |   | \\\\\\  -  /// |   |\n' +
        '            | \\_|  \'\'\\---/\'\'  |   |\n' +
        '            \\  .-\\__  `-`  ___/-. /\n' +
        '          ___`. .\'  /--.--\\  `. . __\n' +
        '       ."" \'<  `.___\\_<|>_/___.\'  >\'"".\n' +
        '      | | :  `- \\`.;`\\ _ /`;.`/ - ` : | |\n' +
        '      \\  \\ `-.   \\_ __\\ /__ _/   .-` /  /\n' +
        ' ======`-.____`-.___\\_____/___.-`____.-\'======\n' +
        '                    `=---=\'\n' +
        ' ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n' +
        '            佛祖保佑       永无BUG\n' +
        '*/',
    monsters:
        '/**\n' +
        ' *      ┌─┐       ┌─┐\n' +
        ' *   ┌──┘ ┴───────┘ ┴──┐\n' +
        ' *   │                 │\n' +
        ' *   │       ───       │\n' +
        ' *   │  ─┬┘       └┬─  │\n' +
        ' *   │                 │\n' +
        ' *   │       ─┴─       │\n' +
        ' *   │                 │\n' +
        ' *   └───┐         ┌───┘\n' +
        ' *       │         │\n' +
        ' *       │         │\n' +
        ' *       │         │\n' +
        ' *       │         └──────────────┐\n' +
        ' *       │                        │\n' +
        ' *       │                        ├─┐\n' +
        ' *       │                        ┌─┘\n' +
        ' *       │                        │\n' +
        ' *       └─┐  ┐  ┌───────┬──┐  ┌──┘\n' +
        ' *         │ ─┤ ─┤       │ ─┤ ─┤\n' +
        ' *         └──┴──┘       └──┴──┘\n' +
        ' *                神兽保佑\n' +
        ' *                永无BUG!\n' +
        ' */\n',

}

const getDefaultState = () => {
    return {
        buff: buff,
    }
}
const state = getDefaultState()


export default {
    namespaced: true,
    state,
}
