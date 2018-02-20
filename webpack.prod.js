/* global require __dirname module */
'use strict';
const webpack = require('webpack');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const UglifyJSPlugin = require('uglifyjs-webpack-plugin');

const path = require('path');
const buildPath = path.resolve(__dirname, 'src/main/resources/static');
const extractCSS = new ExtractTextPlugin(`[name].css`);

module.exports = {
    devtool: 'sourcemaps',
    entry: {
        main: './src/main/js/app/index'
    },
    cache: true,
    output: {
        path: buildPath,
        filename: '[name].js',
        publicPath: '/'
    },
    module: {
        loaders: [
            {
                test: /\.(js|jsx)$/,
                exclude: /(node_modules)/,
                loader: 'babel-loader',
            },
            {
                test: /\.css$/,
                use: extractCSS.extract({
                    fallback: 'style-loader',
                    use: [
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
                })
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
            DEV: 'false',
            'process.env': {
                NODE_ENV: JSON.stringify('production')
            }
        }),
        extractCSS,
        new HtmlWebpackPlugin({
            template: './src/main/js/index.html',
            chunksSortMode: 'dependency',
            inject: 'body'
        }),
        new UglifyJSPlugin({
            parallel: true,
            uglifyOptions: {
                ie8: false,
                compress: {
                    dead_code: true,
                    warnings: false,
                    properties: true,
                    drop_debugger: true,
                    conditionals: true,
                    booleans: true,
                    loops: true,
                    unused: true,
                    toplevel: true,
                    if_return: true,
                    inline: true,
                    join_vars: true
                },
                output: {
                    comments: false,
                    beautify: false,
                    indent_level: 2
                }
            }
        })
    ]
};
