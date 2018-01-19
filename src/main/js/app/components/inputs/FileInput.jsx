import React from 'react';
import styled from 'styled-components';
import { Form } from 'semantic-ui-react';
import * as types from 'app/lib/messageTypes';
const FileButton = styled.label`
    box-sizing: border-box;
    display: inline-block;
    width: 75px;
    cursor: pointer;
    background-color: #2185d0;
    color: #fff !important;
    text-align: center;
    border-radius: 0.28571429rem;
`;

export default class FileInput extends React.Component {
    constructor(props) {
        super(props);
        this.state = { acceptType: '', fileName: '' };
        this.changeHandler = this.changeHandler.bind(this);
    }

    changeHandler(e) {
        // eslint-disable-next-line
        const reader = new FileReader();
        const file = e.target.files[0];
        this.setState({ fileName: file.name });
        reader.onloadend = () => {
            this.props.changeHandler(this.props.type, null, {
                value: JSON.stringify({
                    name: file.name,
                    type: file.type,
                    content: reader.result
                })
            });
        };

        reader.readAsDataURL(file);
    }

    componentDidMount() {
        const { type } = this.props;
        let acceptType = '';
        switch (type) {
            case types.PHOTO:
                acceptType = 'image/*';
                break;
            case types.AUDIO:
                acceptType = 'audio/*';
                break;
            case types.VIDEO:
                acceptType = 'video/*';
                break;
            default:
                break;
        }
        this.setState({ acceptType });
    }

    componentWillUnmount() {
        this.setState({ acceptType: '' });
    }

    render() {
        const { fileName, acceptType } = this.state;
        return (
            <React.Fragment>
                <Form.Field>
                    <input
                        type="file"
                        name="file"
                        multiple={false}
                        accept={acceptType}
                        id="file"
                        onChange={this.changeHandler}
                        style={{ display: 'none' }}
                    />
                    <FileButton htmlFor="file">Choose file</FileButton>
                    {fileName && <span>{fileName}</span>}
                </Form.Field>
            </React.Fragment>
        );
    }
}
