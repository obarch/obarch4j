module.exports = {
    base: '/obarch4j/codec/',
    title: 'QJSON',
    themeConfig: {
        sidebar: [
            '/',
            {
                title: 'Usage',
                children: [
                    '/usage/input_output/input_output',
                    '/usage/struct/struct',
                    '/usage/container/container',
                    '/usage/inf/inf',
                    '/usage/any/any'
                ]
            },
            {
                title: 'SPI',
                children: [
                    '/spi/config',
                    '/spi/encoder/encoder',
                    '/spi/decoder/decoder'
                ]
            },
            {
                title: 'Format',
                children: [
                    '/format/ref/ref'
                ]
            }
        ]
    }
};