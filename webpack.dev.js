/* global require, __dirname, module, process */
'use strict';

const webpack = require('webpack');
const BrowserSyncPlugin = require('browser-sync-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const WebpackNotifierPlugin = require('webpack-notifier');

const path = require('path');
const buildPath = path.resolve(__dirname, 'src/main/resources/static');

module.exports = {
    devtool: 'eval-source-map',
    devServer: {
        contentBase: buildPath,
        historyApiFallback: true,
        proxy: [{
            context: [
                '/api',
                '/auth'
            ],
            target: 'http://127.0.0.1:8080',
            secure: false
        }, {
            context: [
                '/sockjs-node/*'
            ],
            target: 'http://localhost:8080/chat',
            pathRewrite: {'^/sockjs-node': ''},
            ws: true,
            secure: false
        }],
        watchOptions: {
            ignored: /node_modules/
        }
    },
    entry: {
        main: './src/main/js/app/index'
    },
    output: {
        path: buildPath,
        filename: '[name].js',
        publicPath: '/'
    },
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                exclude: /node_modules/,
                loaders: [
                    {
                        loader: 'babel-loader'
                    },
                    {
                        loader: 'eslint-loader',
                        options: {
                            emitWarning: true,
                            failOnWarning: false,
                            failOnError: false,
                        }
                    },
                ]
            },
            {
                test: /\.css$/,
                loaders: [
                    {
                        loader: 'style-loader',
                    },
                    {
                        loader: 'css-loader',
                        options: {
                            importLoaders: 1,
                        }
                    },
                    {
                        loader: 'postcss-loader'
                    }
                ]
            }
        ]
    },
    resolve: {
        modules: [path.resolve(__dirname, "src/main/js"), "node_modules"],
        extensions: ['.js', '.jsx', '.css'],
        alias: {
            app: path.resolve(__dirname, "src/main/js/app"),
            components: path.resolve(__dirname, "src/main/js/app/components"),
            containers: path.resolve(__dirname, "src/main/js/app/containers"),
            constants: path.resolve(__dirname, "src/main/js/app/store/constants"),
            sockets$: path.resolve(__dirname, "src/main/js/app/lib/sockets/index.js")
        }
    },
    plugins: [
        new webpack.DefinePlugin({
            DEV: 'true',
            'process.env': {
                NODE_ENV: JSON.stringify(process.env.NODE_ENV)
            }
        }),
        new HtmlWebpackPlugin({
            template: './src/main/js/index.html',
            chunksSortMode: 'dependency',
            inject: 'body'
        }),
        new BrowserSyncPlugin({
            host: 'localhost',
            port: 9000,
            proxy: {
                target: 'http://localhost:9060',
                ws: true
            }
        }, {
            reload: false
        }),
        new webpack.NoEmitOnErrorsPlugin(),
        new webpack.NamedModulesPlugin(),
        new WebpackNotifierPlugin()
    ]
};
