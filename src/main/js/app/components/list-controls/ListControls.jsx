import React from 'react';
import { Button } from 'semantic-ui-react';

/* eslint-disable react/prop-types */
export default class ListControls extends React.Component {
    constructor(props) {
        super(props);
        this.pollStart = this.pollStart.bind(this);
        this.pollStop = this.pollStop.bind(this);
        this.logout = this.logout.bind(this);
    }
    pollStart() {
        const { pollStart, client: { token } } = this.props;
        pollStart(token, 2000);
    }

    pollStop() {
        this.props.pollStop();
    }

    logout() {
        this.props.unsetClient();
    }

    componentWillUnmount() {
        this.pollStop();
    }

    render() {
        return (
            <div>
                <Button onClick={this.props.fetchAssets}>get assets</Button>
                {this.props.polling ? (
                    <Button onClick={this.pollStop}>poll stop</Button>
                ) : (
                    <Button onClick={this.pollStart}>poll start</Button>
                )}
                <Button onClick={this.logout}>logout</Button>
            </div>
        );
    }
}
